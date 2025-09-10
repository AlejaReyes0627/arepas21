package com.restaurante.arepas21.infrastructure.config.jwtConfig;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    
    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        logger.info("🔍 Processing request: {} {}", method, requestURI);
        
        // Verificar si es una ruta pública (no necesita autenticación)
        if (isPublicPath(requestURI)) {
            logger.info("✅ Public path, skipping authentication: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
        
        String jwt = null;
        
        // 1. Primero intentar obtener token del header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            logger.info("🔑 Token found in Authorization header");
        }
        
        // 2. Si no está en el header, buscar en cookies
        if (jwt == null && request.getCookies() != null) {
            logger.info("🍪 Searching for token in cookies...");
            for (var cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    logger.info("🔑 Token found in cookie");
                    break;
                }
            }
        }
        
        if (jwt != null) {
            String username = null;
            try {
                username = jwtUtil.extractUsername(jwt);
                logger.info("✅ Username extracted from token: {}", username);
            } catch (Exception e) {
                logger.warn("❌ Token JWT inválido: {}", e.getMessage());
                // Continúa sin autenticar
            }
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    logger.info("🔍 Loading user details for: {}", username);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    
                    logger.info("✅ User details loaded:");
                    logger.info("  Username: {}", userDetails.getUsername());
                    logger.info("  Authorities: {}", userDetails.getAuthorities());
                    logger.info("  Enabled: {}", userDetails.isEnabled());
                    
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        logger.info("✅ Token is valid, setting authentication...");
                        
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        
                        // Establecer la autenticación en el contexto de seguridad
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        
                        // Verificar que se estableció correctamente
                        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
                        if (currentAuth != null) {
                            logger.info("✅ Authentication set successfully:");
                            logger.info("  Name: {}", currentAuth.getName());
                            logger.info("  Authorities: {}", currentAuth.getAuthorities());
                            logger.info("  Is authenticated: {}", currentAuth.isAuthenticated());
                        } else {
                            logger.error("❌ Failed to set authentication in SecurityContext");
                        }
                    } else {
                        logger.warn("❌ Token validation failed for user: {}", username);
                    }
                } catch (Exception e) {
                    logger.error("❌ Error during authentication process: {}", e.getMessage());
                }
            } else if (username != null) {
                logger.info("ℹ️ User {} already authenticated", username);
            }
        } else {
            logger.info("ℹ️ No JWT token found for protected route: {}", requestURI);
        }
        
        // Continuar con el siguiente filtro
        filterChain.doFilter(request, response);
    }
    
    /**
     * Determina si una ruta es pública y no requiere autenticación
     */
    private boolean isPublicPath(String path) {
        String[] publicPaths = {
            "/auth/",
            "/swagger-ui",
            "/v3/api-docs",
            "/error",
            "/favicon.ico",
            "/api/test/public" // Para testing
        };
        
        for (String publicPath : publicPaths) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }
        return false;
    }
}