package com.ug.ec.SGHospitalizacion.domain;

import lombok.*;

/**
 * Subdocumento embebido dentro de {@link ExamenMedico}.
 * Corresponde a cada fila de la tabla de Diagnóstico (EM-04) del frontend:
 * CIE / Descripción / PRE (presuntivo) / DEF (definitivo).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiagnosticoMedico {

    private String cie;
    private String descripcion;

    // ? TRUE - el diagnóstico está marcado como presuntivo
    private Boolean presuntivo;

    // ? TRUE - el diagnóstico está marcado como definitivo
    private Boolean definitivo;
}
