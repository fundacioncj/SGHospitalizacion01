package com.ug.ec.SGHospitalizacion.dto.response;

import com.ug.ec.SGHospitalizacion.domain.DiagnosticoMedico;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamenMedicoResponseDTO {

    private String id;
    private LocalDate fechaRegistro;

    // * REFERENCIAS
    private String pacienteId;
    private String nombrePaciente;
    private String cedulaPaciente;

    private String medicoId;
    private String nombreMedico;

    private String historiaClinicaId;
    private String numeroHistoriaClinica;

    // * EM-01
    private String temperatura;
    private String presionArterial;
    private String pulso;
    private String frecuenciaRespiratoria;
    private String peso;
    private String talla;
    private String imc;
    private String perimetroCefalico;
    private String pulsioximetria;

    // * EM-02
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

    // * EM-03
    private String analisis;

    // * EM-04
    private List<DiagnosticoMedico> diagnosticos;

    // * EM-05
    private String planTratamiento;
}