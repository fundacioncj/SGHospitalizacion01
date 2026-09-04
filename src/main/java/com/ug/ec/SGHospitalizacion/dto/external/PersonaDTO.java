package com.ug.ec.SGHospitalizacion.dto.external;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PersonaDTO {

    private Long id;

    private String primerNombre;
    private String segundoNombre;

    private String primerApellido;
    private String segundoApellido;

    private String cedula;
}