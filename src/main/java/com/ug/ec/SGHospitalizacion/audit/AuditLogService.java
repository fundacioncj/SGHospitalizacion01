package com.ug.ec.SGHospitalizacion.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void registrar(AuditLog auditLog) {

        if (auditLog.getFechaHora() == null) {
            auditLog.setFechaHora(LocalDateTime.now());
        }

        if (auditLog.getResultado() == null) {
            auditLog.setResultado("EXITOSO");
        }

        try {
            auditLogRepository.save(auditLog);

            log.info(
                    "AUDITORÍA | módulo={} | acción={} | método={} | endpoint={} | paciente={} | cédula={} | resultado={}",
                    auditLog.getModulo(),
                    auditLog.getAccion(),
                    auditLog.getMetodo(),
                    auditLog.getEndpoint(),
                    auditLog.getPacienteId(),
                    auditLog.getCedulaPaciente(),
                    auditLog.getResultado()
            );

        } catch (Exception e) {

            /*
             * IMPORTANTE:
             * Si falla el registro de auditoría NO debe fallar
             * la operación clínica principal.
             */
            log.error(
                    "No se pudo guardar el registro de auditoría: {}",
                    e.getMessage(),
                    e
            );
        }
    }
}