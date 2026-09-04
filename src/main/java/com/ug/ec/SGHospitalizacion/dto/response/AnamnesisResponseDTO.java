package com.ug.ec.SGHospitalizacion.dto.response;

import com.ug.ec.SGHospitalizacion.domain.GinecoObstetricosAndrologicos;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnamnesisResponseDTO {

    private String id;
    private LocalDate fechaRegistro;

    // * A: REFERENCIAS ---------------------------------------------------------------------------
    private String pacienteId;
    private String nombrePaciente;
    private String cedulaPaciente;

    private String medicoId;
    private String nombreMedico;

    private String historiaClinicaId;

    // * B: MOTIVO DE LA CONSULTA -----------------------------------------------------------------
    private List<String> motivosConsulta;

    // * C: ANTECEDENTES PATOLÓGICOS PERSONALES ---------------------------------------------------
    private Boolean alergiaMedicamentos;            // ? TRUE - EL PACIENTE REFIERE TAL ANTECEDENTE
    private Boolean otrasAlergias;
    private Boolean vacunas;
    private Boolean patologiasClinicas;
    private Boolean medicacionHabitual;
    private Boolean antecedentesQuirurgicos;
    private Boolean habitos;
    private Boolean condicionSocioeconomica;
    private Boolean discapacidad;
    private Boolean religion;
    private Boolean tipificacionSanguinea;

    // Descripciones libres de cada sección marcada (opcional por sección)
    private String descripcionAlergias;
    private String descripcionVacunas;
    private String descripcionPatologiasClinicas;
    private String descripcionMedicacionHabitual;
    private String descripcionAntecedentesQuirurgicos;
    private String descripcionHabitos;
    private String descripcionCondicionSocioeconomica;
    private String descripcionDiscapacidad;
    private String descripcionReligion;
    private String descripcionTipificacionSanguinea;

    // ? Sección de Gineco-Obstétricos y Andrológicos
    private GinecoObstetricosAndrologicos ginecoObstetricosAndrologicos;

    // ? Sección "NO REFIERE" en el formulario
    private Boolean antecedentesPersonalesNoRefiere;

    // * D: ANTECEDENTES PATOLÓGICOS FAMILIARES ----------------------------------------------------
    private Boolean familiarCardiopatia;             // ? TRUE - EL PACIENTE REFIERE TAL ANTECEDENTE
    private Boolean familiarHipertension;
    private Boolean familiarEnfermedadCerebroVascular;
    private Boolean familiarEndocrinoMetabolico;
    private Boolean familiarCancer;
    private Boolean familiarTuberculosis;
    private Boolean familiarEnfermedadMental;
    private Boolean familiarEnfermedadInfecciosa;
    private Boolean familiarMalformacion;
    private Boolean familiarOtro;
    private String  familiarOtroDescripcion;

    // ? Sección "NO REFIERE" en el formulario
    private Boolean antecedentesFamiliaresNoRefiere;

    // * E: ENFERMEDAD O PROBLEMA ACTUAL -----------------------------------------------------------
    private String enfermedadActual;

    // * F: REVISIÓN ACTUAL DE ÓRGANOS Y SISTEMAS --------------------------------------------------
    private Boolean sistemaPielAnexos;              // ? TRUE - EL PACIENTE REFIERE CIERTA PATOLOGÍA
    private Boolean sistemaOrganosDeLosSentidos;
    private Boolean sistemaRespiratorio;
    private Boolean sistemaCardiovascular;
    private Boolean sistemaDigestivo;
    private Boolean sistemaGenitoUrinario;
    private Boolean sistemaMusculoEsqueletico;
    private Boolean sistemaEndocrino;
    private Boolean sistemaHemoLinfatico;
    private Boolean sistemaNervioso;

    // Descripciones por sistema con patología
    private String descripcionPielAnexos;
    private String descripcionOrganosDeLosSentidos;
    private String descripcionRespiratorio;
    private String descripcionCardiovascular;
    private String descripcionDigestivo;
    private String descripcionGenitoUrinario;
    private String descripcionMusculoEsqueletico;
    private String descripcionEndocrino;
    private String descripcionHemoLinfatico;
    private String descripcionNervioso;
}
