package com.ug.ec.SGHospitalizacion.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class TokenJwtConfig {

    public static SecretKey SECRET_KEY;

    @Value("${JWT_SECRET}")
    public void setJwtSecret(String secretBase64) {
        SECRET_KEY = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secretBase64)
        );
    }

    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
}