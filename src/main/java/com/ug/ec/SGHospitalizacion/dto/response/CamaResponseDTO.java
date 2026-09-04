package com.ug.ec.SGHospitalizacion.dto.response;

import com.ug.ec.SGHospitalizacion.domain.enums.EstadoCama;
import com.ug.ec.SGHospitalizacion.domain.enums.TipoCama;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamaResponseDTO {
    private String id;
    private String numeroCama;
    private String sala;
    private Integer piso;
    private TipoCama tipo;
    private EstadoCama estado;

    //sera nulo cuando este libre y se le agrega cuando el servicio de hospitalizacion le asigne la cama
    private String hospitalizacionId;
    private String descripcion;
}
