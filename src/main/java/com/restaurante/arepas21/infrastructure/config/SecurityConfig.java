package com.restaurante.arepas21.infrastructure.config;


import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import com.restaurante.arepas21.application.service.CustomUserDetailsServiceImpl;
import com.restaurante.arepas21.infrastructure.config.jwtConfig.JwtAuthFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Importante: habilitar @PreAuthorize
public class SecurityConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomUserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider,
            CustomUserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.userDetailsService = userDetailsService;
        
        logger.info("🔧 SecurityConfig initialized");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("🔧 Configuring SecurityFilterChain...");
        
        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                logger.info("  Configuring authorization rules...");
                auth
                    // Rutas públicas - sin autenticación
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/favicon.ico").permitAll()
                    .requestMatchers("/api/test/public").permitAll() // Ruta de testing pública
                    
                    // Todas las demás rutas requieren autenticación
                    .anyRequest().authenticated();
                    
                logger.info("  ✅ Authorization rules configured");
            })
            .sessionManagement(sess -> {
                logger.info("  Configuring stateless session management...");
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .authenticationProvider(authenticationProvider)
            .userDetailsService(userDetailsService)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptions -> {
                logger.info("  Configuring exception handling...");
                exceptions
                    .authenticationEntryPoint((request, response, authException) -> {
                        logger.warn("❌ Authentication failed for {}: {}", 
                            request.getRequestURI(), authException.getMessage());
                        response.sendError(401, "Authentication required");
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        logger.warn("❌ Access denied for {}: {}", 
                            request.getRequestURI(), accessDeniedException.getMessage());
                        response.sendError(403, "Access denied");
                    });
            })
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        logger.info("🔧 Configuring AuthenticationManager...");
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        logger.info("🔧 Configuring CORS...");
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir orígenes específicos
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",
            "http://localhost:3000",
            "http://localhost:8080"
        ));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Permitir credenciales (importante para cookies)
        configuration.setAllowCredentials(true);
        
        // Headers expuestos
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization", "Content-Type"
        ));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        logger.info("✅ CORS configured successfully");
        return source;
    }
}