package com.ug.ec.SGHospitalizacion.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamenMedicoRequestDTO {

    // ── REFERENCIAS ───────────────────────────────────────────────────────────
    @NotBlank(message = "La cédula del paciente es obligatoria")
    private String cedulaPaciente;

    @NotBlank(message = "La cédula del médico es obligatoria")
    private String cedulaMedico;

    @NotNull(message = "La fecha de registro es obligatoria")
    private LocalDate fechaRegistro;

    // * EM-01: CONSTANTES VITALES Y ANTROPOMETRÍA
    private String temperatura;
    private String presionArterial;
    private String pulso;
    private String frecuenciaRespiratoria;
    private String peso;
    private String talla;
    private String imc;
    private String perimetroCefalico;
    private String pulsioximetria;

    // * EM-02: EXAMEN FÍSICO REGIONAL
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

    // * Revisión de sistemas
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

    // * EM-03: ANÁLISIS
    private String analisis;

    // * EM-04: DIAGNÓSTICO
    private List<DiagnosticoMedicoRequestDTO> diagnosticos;

    // * EM-05: PLAN DE TRATAMIENTO
    private String planTratamiento;
}
