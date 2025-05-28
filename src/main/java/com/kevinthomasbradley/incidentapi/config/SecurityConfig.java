package com.kevinthomasbradley.incidentapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import com.kevinthomasbradley.incidentapi.JwtAuthenticationFilter;
import com.kevinthomasbradley.incidentapi.JwtUtil;

/**
 * Security configuration class for the Incidentra application.
 * <p>
 * Configures authentication, authorization, session management, password encoding,
 * and registers the JWT authentication filter for stateless security.
 * </p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Service for loading user-specific data during authentication.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Registers the JWT authentication filter as a Spring bean.
     * This filter intercepts requests to validate JWT tokens and set authentication in the security context.
     *
     * @param jwtUtil the utility class for JWT operations
     * @param userDetailsService the service to load user details
     * @return the configured JwtAuthenticationFilter bean
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService);
    }

    /**
     * Configures the security filter chain for HTTP requests.
     * <ul>
     *   <li>Disables CSRF protection (suitable for stateless APIs).</li>
     *   <li>Defines authorization rules for endpoints based on user roles.</li>
     *   <li>Sets session management to stateless (no HTTP session is created).</li>
     *   <li>Registers the authentication provider and JWT filter.</li>
     * </ul>
     *
     * @param http the HttpSecurity object to configure
     * @param jwtAuthFilter the JWT authentication filter bean
     * @return the configured SecurityFilterChain bean
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // Allow unauthenticated access to auth endpoints
                .requestMatchers(HttpMethod.POST, "/api/incidents").hasRole("CITIZEN") // Only CITIZEN can create incidents
                .requestMatchers(HttpMethod.PUT, "/api/incidents/**/assign").hasRole("DISPATCHER") // Only DISPATCHER can assign
                .requestMatchers(HttpMethod.PUT, "/api/incidents/**/resolve").hasRole("RESPONDER") // Only RESPONDER can resolve
                .anyRequest().authenticated() // All other requests require authentication
            )
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No HTTP session; JWT only
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures the password encoder bean using BCrypt.
     * This encoder is used to hash and verify user passwords securely.
     *
     * @return the BCryptPasswordEncoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication provider for DAO-based authentication.
     * Uses the custom UserDetailsService and BCrypt password encoder.
     *
     * @return the configured AuthenticationProvider bean
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Exposes the AuthenticationManager bean for use in authentication endpoints (e.g., login).
     *
     * @param config the AuthenticationConfiguration provided by Spring Boot
     * @return the AuthenticationManager bean
     * @throws Exception if an error occurs during retrieval
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
