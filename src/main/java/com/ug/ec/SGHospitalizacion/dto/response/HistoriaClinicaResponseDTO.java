package com.ug.ec.SGHospitalizacion.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriaClinicaResponseDTO {

    private String id;
    private String pacienteId;
    private String nombrePaciente;
    private String cedulaPaciente;
    private String numeroHistoriaClinica;
    private LocalDate fechaApertura;
    private List<String> alergias;
    private List<String> antecedentesMedicos;
    private List<String> antecedentesQuirurgicos;
    private List<String> antecedentesFamiliares;
    private List<String> medicamentosActuales;
    private String observaciones;
    private Boolean activo;
}
