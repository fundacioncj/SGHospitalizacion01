package com.ug.ec.SGHospitalizacion.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosticoMedicoRequestDTO {

    private String cie;
    private String descripcion;
    private Boolean presuntivo;
    private Boolean definitivo;
}
