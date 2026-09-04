package com.ug.ec.SGHospitalizacion.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de RestTemplate para comunicación con SGBITAMEDICA03.
 * La URL base se configura en application.properties con la propiedad:
 *   sgbitamedica.base-url=http://localhost:8081
 */
@Configuration
public class RestClientConfig {

    @Value("${sgbitamedica.base-url:http://localhost:8081}")
    private String sgBitaMedicaBaseUrl;

    /**
     * Bean RestTemplate para uso general.
     * Se puede personalizar con timeouts si se requiere.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Expone la URL base del sistema externo como bean String.
     * Los clientes REST la inyectan con @Value o vía este bean nombrado.
     */
    @Bean(name = "sgbBaseUrl")
    public String sgbBaseUrl() {
        return sgBitaMedicaBaseUrl;
    }
}
