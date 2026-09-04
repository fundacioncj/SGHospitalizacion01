package com.ug.ec.SGHospitalizacion.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "historias_clinicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriaClinica {

    @Id
    private String id;

    // IDENTIFICACIÓN PACIENTE
    @Indexed(unique = true)
    private String pacienteId;

    // NÚMERO DE HC DE CADA PACIENTE
    @Indexed(unique = true)
    private String numeroHistoriaClinica;

    private LocalDate fechaApertura;

    // ANTECEDENTES CLINICOS DE PACIENTE
    @Builder.Default
    private List<String> alergias = new ArrayList<>();

    @Builder.Default
    private List<String> antecedentesMedicos = new ArrayList<>();

    @Builder.Default
    private List<String> antecedentesQuirurgicos = new ArrayList<>();

    @Builder.Default
    private List<String> antecedentesFamiliares = new ArrayList<>();

    @Builder.Default
    private List<String> medicamentosActuales = new ArrayList<>();

    // OBSERVACIONES
    private String observaciones;

    // ACTIVO | INACTIVO
    @Builder.Default
    private Boolean activo = true;
}
