package com.ug.ec.SGHospitalizacion.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GinecoObstetricosAndrologicosRequestDTO {

    // ── DATOS GINECOLÓGICOS ───────────────────────────────────────────────────
    private Integer edadMenarquia;
    private Integer edadMenopausia;
    private String  ciclos;                    // ej. "regular 28/5"
    private Integer edadInicioVidaSexual;
    private Integer numeroGestas;
    private Integer numeroPartos;
    private Integer numeroAbortos;
    private Integer numeroCesareas;
    private Integer numeroHijosVivos;
    private LocalDate fechaUltimaMenstruacion;
    private LocalDate fechaUltimoParto;
    private LocalDate fechaUltimaCitologiaCervical;
    private LocalDate fechaUltimaColposcopia;
    private LocalDate fechaUltimaMamografia;
    private String  metodoPlanificacionFamiliar;
    private String  terapiaHormonal;

    // ── DATOS ANDROLÓGICOS ────────────────────────────────────────────────────
    private LocalDate fechaUltimoAntigenoProstatico;
    private LocalDate fechaUltimoEcoProstatico;
}
