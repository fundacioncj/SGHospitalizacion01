package com.ug.ec.SGHospitalizacion.dto.external;

import com.ug.ec.SGHospitalizacion.infrastructure.client.SgbCitaMedicaClient;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DTO de referencia para datos de Paciente obtenidos de SGBITAMEDICA03.
 * NO se persiste en MongoDB; se usa solo para validación y enriquecimiento de respuestas.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PacienteExternoDTO {

    private Long id;

    private PersonaDTO persona;

    private String telefono;
    private String correo;
    private Boolean activo;

    /**
     * Mantener este método para no romper el código actual
     */
    public String getCedula() {

        return persona != null
                ? persona.getCedula()
                : null;
    }


    /**
     * Nombre completo calculado
     */
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
