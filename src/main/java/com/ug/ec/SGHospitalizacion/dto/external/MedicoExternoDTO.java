package com.ug.ec.SGHospitalizacion.dto.external;

import lombok.*;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DTO de referencia para datos de Médico obtenidos de SGBITAMEDICA03.
 * NO se persiste en MongoDB; se usa solo para validación y enriquecimiento de respuestas.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MedicoExternoDTO {

    private Long id;          // ID SQL de SGBITAMEDICA03
    private PersonaDTO persona; // MODIFICANDO
    private String especialidad;
    private String telefono;
    private String correo;
    private Boolean activo;

    public String getCedula() {

        return persona != null
                ? persona.getCedula()
                : null;
    }


    public String getNombreCompleto() {

        if (persona == null) {
            return "";
        }

        return Stream.of(
                        persona.getPrimerNombre(),
                        persona.getSegundoNombre(),
                        persona.getPrimerApellido(),
                        persona.getSegundoApellido()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }

    public String getNombres() {
        return persona != null ? persona.getPrimerNombre() : null;
    }

    public String getApellidos() {

        if (persona == null) {
            return null;
        }

        return Stream.of(
                        persona.getPrimerApellido(),
                        persona.getSegundoApellido()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }
}
