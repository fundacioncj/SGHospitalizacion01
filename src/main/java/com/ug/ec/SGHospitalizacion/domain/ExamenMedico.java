package com.ug.ec.SGHospitalizacion.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Siguiendo el Formulario 003 - Examen Médico
 * Secciones a tratar:
 *   EM-01: Constantes vitales y antropometría
 *   EM-02: Examen físico regional
 *   EM-03: Análisis
 *   EM-04: Diagnóstico
 *   EM-05: Plan de tratamiento
 */
@Document(collection = "examen_medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamenMedico {

    @Id
    private String id;

    // * ── Entidades que interactúan
    @Indexed
    private String pacienteId;

    @Indexed
    private String medicoId;

    @Indexed
    private String historiaClinicaId;

    @Indexed
    private String numeroHistoriaClinica;

    private LocalDate fechaRegistro;

    // * EM-01: CONSTANTES VITALES Y ANTROPOMETRÍA -------------------------------------------------
    private String temperatura;
    private String presionArterial;
    private String pulso;
    private String frecuenciaRespiratoria;
    private String peso;
    private String talla;
    private String imc;
    private String perimetroCefalico;
    private String pulsioximetria;

    // * EM-02: EXAMEN FÍSICO REGIONAL --------------------------------------------------------------

    private Boolean pielFaneras;
    private String descripcionPielFaneras;

    private Boolean cabeza;
    private String descripcionCabeza;

    private Boolean ojos;
    private String descripcionOjos;

    private Boolean oidos;
    private String descripcionOidos;

    private Boolean nariz;
    private String descripcionNariz;

    private Boolean boca;
    private String descripcionBoca;

    private Boolean orofaringe;
    private String descripcionOrofaringe;

    private Boolean cuello;
    private String descripcionCuello;

    private Boolean axilasMamas;
    private String descripcionAxilasMamas;

    private Boolean torax;
    private String descripcionTorax;

    private Boolean abdomen;
    private String descripcionAbdomen;

    private Boolean columnaVertebral;
    private String descripcionColumnaVertebral;

    private Boolean inglePerine;
    private String descripcionInglePerine;

    private Boolean miembrosSuperiores;
    private String descripcionMiembrosSuperiores;

    private Boolean miembrosInferiores;
    private String descripcionMiembrosInferiores;


// ── Revisión de sistemas (1S..10S) ──────────────────────────────────────────────

    private Boolean organosSentidos;
    private String descripcionOrganosSentidos;

    private Boolean respiratorio;
    private String descripcionRespiratorio;

    private Boolean cardioVascular;
    private String descripcionCardioVascular;

    private Boolean digestivo;
    private String descripcionDigestivo;

    private Boolean genital;
    private String descripcionGenital;

    private Boolean urinario;
    private String descripcionUrinario;

    private Boolean musculoEsqueletico;
    private String descripcionMusculoEsqueletico;

    private Boolean endocrino;
    private String descripcionEndocrino;

    private Boolean hemoLinfatico;
    private String descripcionHemoLinfatico;

    private Boolean neurologico;
    private String descripcionNeurologico;

    // * EM-03: ANÁLISIS -----------------------------------------------------------------------------
    private String analisis;

    // * EM-04: DIAGNÓSTICO --------------------------------------------------------------------------
    @Builder.Default
    private List<DiagnosticoMedico> diagnosticos = new ArrayList<>();

    // * EM-05: PLAN DE TRATAMIENTO ------------------------------------------------------------------
    private String planTratamiento;
}
