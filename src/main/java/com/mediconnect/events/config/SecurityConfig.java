package com.mediconnect.events.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    // ✅ FIXED (NO LAMBDA → SPRING 4 COMPATIBLE)
    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {

        return new Converter<Jwt, AbstractAuthenticationToken>() {

            @Override
            public AbstractAuthenticationToken convert(Jwt jwt) {

                Collection<String> roles = new ArrayList<>();

                Map<String, Object> realmAccess = jwt.getClaim("realm_access");

                if (realmAccess != null && realmAccess.containsKey("roles")) {
                    roles = (Collection<String>) realmAccess.get("roles");
                }

                Collection<org.springframework.security.core.GrantedAuthority> authorities = new ArrayList<>();

                for (String role : roles) {
                    authorities.add(
                            new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role)
                    );
                }

                return new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken(
                        jwt,
                        authorities
                );
            }
        };
    }
}