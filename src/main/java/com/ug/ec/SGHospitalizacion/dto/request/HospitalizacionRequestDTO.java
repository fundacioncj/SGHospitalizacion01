package com.ug.ec.SGHospitalizacion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class HospitalizacionRequestDTO {
    @NotBlank(message = "La cédula del paciente es obligatoria")
    private String cedulaPaciente;

    @NotBlank(message = "La cédula del médico es obligatoria")
    private String cedulaMedico;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate fechaIngreso;

    @NotBlank(message = "El motivo de ingreso es obligatorio")
    private String motivoIngreso;

    @NotBlank(message = "El diagnóstico principal es obligatorio")
    private String diagnosticoPrincipal;

    @NotBlank(message = "La sala/habitación es obligatoria")
    private String salaHabitacion;

    private String observaciones;
}
