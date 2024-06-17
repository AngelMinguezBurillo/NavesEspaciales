package com.navesespaciales.shared.infra.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navesespaciales.shared.domain.exc.ConfiguracionException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;
    
    @Bean
    public SecurityFilterChain filterChain(
            final HttpSecurity httpSecurity,
            final ObjectMapper objectMapper,
            final MessagesResourceService messagesResourceService) {
        try {
            return httpSecurity
                    .cors(AbstractHttpConfigurer::disable)
                    .csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(requests -> requests
            .requestMatchers("/info").permitAll()
            .requestMatchers("/health").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/swagger-ui.html").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
            .anyRequest().authenticated()
        ).addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class).build();
        } catch (Exception exc) {
            throw new ConfiguracionException("Error al cargar configuracion de seguirdad", exc);
        }
    }

}
