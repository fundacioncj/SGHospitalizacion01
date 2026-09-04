package com.ug.ec.SGHospitalizacion.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> {})

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(authorize -> authorize

                        // Swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**"
                        ).permitAll()

                        // CORS
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        .requestMatchers("/home").permitAll()

                        // GET
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/**"
                        ).hasAnyRole(
                                "EMPLEADO",
                                "MEDICO",
                                "ENFERMERA",
                                "ADMIN"
                        )

                        // POST
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/**"
                        ).hasAnyRole(
                                "EMPLEADO",
                                "MEDICO",
                                "ENFERMERA",
                                "ADMIN"
                        )

                        // PUT
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/**"
                        ).hasAnyRole(
                                "EMPLEADO",
                                "MEDICO",
                                "ENFERMERA",
                                "ADMIN"
                        )

                        // DELETE
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/**"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        new JwtValidationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}