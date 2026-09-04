package com.ug.ec.SGHospitalizacion.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "audit_logs")
public class AuditLog {

    @Id
    private String id;

    private LocalDateTime fechaHora;

    private String modulo;

    private String accion;

    private String metodo;

    private String endpoint;

    private String recursoId;

    private String pacienteId;

    private String cedulaPaciente;

    private String medicoId;

    private String cedulaMedico;

    private String descripcion;

    private String resultado;

    private Integer estadoHttp;

    private String error;
}