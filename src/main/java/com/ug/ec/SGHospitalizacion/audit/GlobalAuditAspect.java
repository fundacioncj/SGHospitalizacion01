package com.ug.ec.SGHospitalizacion.audit;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class GlobalAuditAspect {

    private final AuditLogRepository auditLogRepository;

    @Around("execution(* com.ug.ec.SGHospitalizacion..controller..*(..))")
    public Object auditarOperacion(ProceedingJoinPoint joinPoint) throws Throwable {

        LocalDateTime fechaHora = LocalDateTime.now();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String clase = signature.getDeclaringType().getSimpleName();
        String metodo = signature.getName();

        HttpServletRequest request = obtenerRequest();

        String endpoint = request != null
                ? request.getRequestURI()
                : "N/A";

        String modulo = obtenerModulo(clase);

        String accion = obtenerAccion(metodo);

        Object resultado;

        try {

            resultado = joinPoint.proceed();

            Integer estadoHttp = obtenerEstadoHttp(resultado);

            AuditLog auditLog = AuditLog.builder()
                    .fechaHora(fechaHora)
                    .modulo(modulo)
                    .accion(accion)
                    .metodo(metodo)
                    .endpoint(endpoint)
                    .descripcion("Ejecución exitosa del método " + metodo)
                    .resultado("EXITOSO")
                    .estadoHttp(estadoHttp)
                    .build();

            auditLogRepository.save(auditLog);

            return resultado;

        } catch (Throwable ex) {

            AuditLog auditLog = AuditLog.builder()
                    .fechaHora(fechaHora)
                    .modulo(modulo)
                    .accion(accion)
                    .metodo(metodo)
                    .endpoint(endpoint)
                    .descripcion("Error durante la ejecución del método " + metodo)
                    .resultado("ERROR")
                    .estadoHttp(500)
                    .error(ex.getMessage())
                    .build();

            auditLogRepository.save(auditLog);

            throw ex;
        }
    }

    private HttpServletRequest obtenerRequest() {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return null;
        }

        return attributes.getRequest();
    }

    private Integer obtenerEstadoHttp(Object resultado) {

        if (resultado instanceof ResponseEntity<?>) {
            return ((ResponseEntity<?>) resultado).getStatusCode().value();
        }

        return 200;
    }

    private String obtenerModulo(String clase) {

        if (clase == null || clase.isBlank()) {
            return "DESCONOCIDO";
        }

        String nombre = clase.toLowerCase();

        if (nombre.contains("hospitalizacion")) {
            return "HOSPITALIZACION";
        }

        if (nombre.contains("anamnesis")) {
            return "ANAMNESIS";
        }

        if (nombre.contains("examen")) {
            return "EXAMEN_MEDICO";
        }

        if (nombre.contains("historia")) {
            return "HISTORIA_CLINICA";
        }

        if (nombre.contains("paciente")) {
            return "PACIENTE";
        }

        if (nombre.contains("medico")) {
            return "MEDICO";
        }

        return clase.replace("Controller", "").toUpperCase();
    }

    private String obtenerAccion(String metodo) {

        String nombre = metodo.toLowerCase();

        if (nombre.startsWith("crear")
                || nombre.startsWith("guardar")
                || nombre.startsWith("registrar")
                || nombre.startsWith("agregar")
                || nombre.startsWith("insertar")) {

            return "CREAR";
        }

        if (nombre.startsWith("actualizar")
                || nombre.startsWith("editar")
                || nombre.startsWith("modificar")) {

            return "ACTUALIZAR";
        }

        if (nombre.startsWith("eliminar")
                || nombre.startsWith("borrar")
                || nombre.startsWith("delete")) {

            return "ELIMINAR";
        }

        if (nombre.startsWith("buscar")
                || nombre.startsWith("obtener")
                || nombre.startsWith("consultar")
                || nombre.startsWith("listar")
                || nombre.startsWith("find")
                || nombre.startsWith("get")) {

            return "CONSULTAR";
        }

        return "EJECUTAR";
    }
}