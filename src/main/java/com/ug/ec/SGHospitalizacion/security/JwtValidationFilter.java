package com.ug.ec.SGHospitalizacion.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.ug.ec.SGHospitalizacion.security.TokenJwtConfig.CONTENT_TYPE;
import static com.ug.ec.SGHospitalizacion.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.ug.ec.SGHospitalizacion.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.ug.ec.SGHospitalizacion.security.TokenJwtConfig.SECRET_KEY;

public class JwtValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(HEADER_AUTHORIZATION);

        System.out.println("=== JWT FILTER ===");
        System.out.println("Authorization: " + header);

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(
                        "COOKIE RECIBIDA: " + cookie.getName()
                );
            }
        } else {
            System.out.println("NO HAY COOKIES");
        }

        String token = null;

        // 1. Buscar JWT en Authorization
        if (header != null && header.startsWith(PREFIX_TOKEN)) {
            token = header.substring(PREFIX_TOKEN.length());
        }

        // 2. Si no existe, buscar JWT en cookie
        if (token == null && cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                    System.out.println("JWT ENCONTRADO EN COOKIE");
                    break;
                }
            }
        }

        // 3. Si no hay JWT, continuar sin autenticación
        if (token == null || token.isBlank()) {
            System.out.println("NO HAY JWT");
            chain.doFilter(request, response);
            return;
        }

        System.out.println("TOKEN RECIBIDO");

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();
            request.setAttribute("username", username);
            Object authoritiesClaims = claims.get("authorities");

            System.out.println("JWT VALIDO");
            System.out.println("Usuario: " + username);
            System.out.println("Authorities: " + authoritiesClaims);

            Collection<? extends GrantedAuthority> authorities =
                    Arrays.asList(
                            new ObjectMapper()
                                    .addMixIn(
                                            SimpleGrantedAuthority.class,
                                            SimpleGrantedAuthorityJsonCreator.class
                                    )
                                    .readValue(
                                            authoritiesClaims
                                                    .toString()
                                                    .getBytes(),
                                            SimpleGrantedAuthority[].class
                                    )
                    );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            System.out.println("AUTHENTICATION:");
            System.out.println(
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
            );

            chain.doFilter(request, response);

        } catch (JwtException | IllegalArgumentException e) {

            Map<String, String> json = new HashMap<>();

            json.put("error", e.getMessage());
            json.put(
                    "message",
                    "El token JWT no es valido"
            );

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType(CONTENT_TYPE);

            response.getWriter().write(
                    new ObjectMapper()
                            .writeValueAsString(json)
            );
        }
    }
}