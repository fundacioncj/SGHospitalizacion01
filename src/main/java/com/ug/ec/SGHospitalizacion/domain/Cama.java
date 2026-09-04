package com.ug.ec.SGHospitalizacion.domain;

import com.ug.ec.SGHospitalizacion.domain.enums.EstadoCama;
import com.ug.ec.SGHospitalizacion.domain.enums.TipoCama;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "camas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Cama {
    @Id
    private String id;

    // Identificador físico único de la cama, ej: "204-B", "UCI-03"
    @Indexed(unique = true)
    private String numeroCama;

    private String sala;
    private Integer piso;
    private TipoCama tipo;

    @Builder.Default
    private EstadoCama estado = EstadoCama.DISPONIBLE;

    // Se asigna al registrar una hospitalización; null cuando la cama está libre.
    // El servicio de hospitalizacion es el único que escribe este campo.
    private String hospitalizacionId;
    private String descripcion;
}
