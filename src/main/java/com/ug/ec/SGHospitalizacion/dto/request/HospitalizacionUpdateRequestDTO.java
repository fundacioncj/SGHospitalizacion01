package com.ug.ec.SGHospitalizacion.dto.request;

import com.ug.ec.SGHospitalizacion.domain.enums.EstadoHospitalizacion;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class HospitalizacionUpdateRequestDTO {

    // Solo se permiten actualizar estos campos después del ingreso
    private LocalDate fechaEgreso;
    private String diagnosticoPrincipal;
    private String salaHabitacion;
    private EstadoHospitalizacion estado;   // ALTA, TRASLADADO, FALLECIDO, etc.
    private String observaciones;

}
