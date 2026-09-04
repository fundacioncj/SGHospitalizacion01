package com.ug.ec.SGHospitalizacion.dto.response;

import com.ug.ec.SGHospitalizacion.domain.enums.EstadoHospitalizacion;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class HospitalizacionResponseDTO {

    private String id;
    private String numeroIngreso;

    // PACIENTE
    private String pacienteId;
    private String nombrePaciente;   // enriquecido por el servicio
    private String cedulaPaciente;   // enriquecido por el servicio

    // MÉDICO
    private String medicoId;
    private String nombreMedico;     // enriquecido por el servicio

    // FECHAS
    private LocalDate fechaIngreso;
    private LocalDate fechaEgreso;

    // CLÍNICA
    private String motivoIngreso;
    private String diagnosticoPrincipal;
    private String salaHabitacion;
    private EstadoHospitalizacion estado;
    private String observaciones;
}
