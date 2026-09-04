package com.ug.ec.SGHospitalizacion.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriaClinicaUpdateRequestDTO {

    private List<String> alergias;
    private List<String> antecedentesMedicos;
    private List<String> antecedentesQuirurgicos;
    private List<String> antecedentesFamiliares;
    private List<String> medicamentosActuales;
    private String observaciones;
    private Boolean activo;
}
