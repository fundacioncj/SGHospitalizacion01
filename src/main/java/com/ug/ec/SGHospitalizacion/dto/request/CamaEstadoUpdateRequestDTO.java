package com.ug.ec.SGHospitalizacion.dto.request;

import com.ug.ec.SGHospitalizacion.domain.enums.EstadoCama;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamaEstadoUpdateRequestDTO {
    // El admin solo puede mover la cama entre DISPONIBLE y MANTENIMIENTO.
    // El estado OCUPADA lo gestiona exclusivamente el servicio de hospitalizacion.
    @NotNull(message = "El estado es obligatorio") private EstadoCama estado;
    //motivo del cambio
    private String descripcion;
}
