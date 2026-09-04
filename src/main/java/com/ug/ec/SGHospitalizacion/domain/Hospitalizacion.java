package com.ug.ec.SGHospitalizacion.domain;

import com.ug.ec.SGHospitalizacion.domain.enums.EstadoHospitalizacion;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "hospitalizaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Hospitalizacion {
    @Id
    private String id;

    // NÚMERO DE INGRESO AUTOGENERADO  (ej: HOSP-000001)
    @Indexed(unique = true)
    private String numeroIngreso;

    // REFERENCIAS
    private String pacienteId;
    private String medicoId;         // ID del Profesional con rol MEDICO

    // FECHAS
    private LocalDate fechaIngreso;
    private LocalDate fechaEgreso;   // null mientras siga hospitalizado

    // INFORMACIÓN CLÍNICA
    private String motivoIngreso;
    private String diagnosticoPrincipal;
    private String salaHabitacion;

    // ESTADO
    @Builder.Default
    private EstadoHospitalizacion estado = EstadoHospitalizacion.ACTIVO;

    private String observaciones;
}
