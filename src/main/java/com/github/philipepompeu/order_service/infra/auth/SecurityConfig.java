package com.github.philipepompeu.order_service.infra.auth;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final SecurityFilter securityFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;    

    public SecurityConfig(SecurityFilter securityFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint){
        this.securityFilter = securityFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
        
        String[] swaggerPattern = { "/swagger-ui.html",
                                    "/v3/api-docs/**",
                                    "/v3/api-docs.yaml",
                                    "/swagger-ui/**",
                                    "/swagger-ui/index.html",
                                    "/swagger-ui/**"};
        http
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilita CORS
        .csrf(csrf -> csrf.disable()) // Desabilita CSRF para facilitar testes com Postman
        .exceptionHandling(ex-> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Configura o entry point para autenticação(força retornar 401 na ausência do token)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
        .requestMatchers(swaggerPattern).permitAll()
        .requestMatchers("/actuator/prometheus").permitAll() //libera pra monitoramento do prometheus
        .requestMatchers(HttpMethod.POST, "/login").permitAll()
        .anyRequest().authenticated()
        ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("http://*");        
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
