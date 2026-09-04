package com.ug.ec.SGHospitalizacion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Lanzada cuando la comunicación con SGBITAMEDICA03 falla o devuelve un error inesperado.
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class IntegracionExternaException extends RuntimeException {

    public IntegracionExternaException(String mensaje) {
        super(mensaje);
    }

    public IntegracionExternaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
