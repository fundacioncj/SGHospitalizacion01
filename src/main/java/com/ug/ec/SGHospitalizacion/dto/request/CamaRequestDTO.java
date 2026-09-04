package com.ug.ec.SGHospitalizacion.dto.request;

import com.ug.ec.SGHospitalizacion.domain.enums.TipoCama;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamaRequestDTO {
    @NotBlank(message = "El número de cama es obligatorio")
    private String numeroCama;

    @NotBlank(message = "La sala es obligatoria")
    private String sala;

    @NotNull(message = "El piso es obligatorio")
    private Integer piso;

    @NotNull(message = "El tipo de cama es obligatorio")
    private TipoCama tipo;

    private String descripcion;
}
