package com.restaurante.arepas21.infrastructure.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Arepas La 21")
                        .version("1.0")
                        .description("Documentación de la API para gestión de pedidos y reportes del restaurante")
                        .contact(new Contact()
                                .name("Desarrollo")
                                .email("aleja.reyes0627@hotmail.com")))
                .components(new Components()
                        .addSecuritySchemes("jwtAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Autenticación JWT para acceso a endpoints protegidos")))
                .addSecurityItem(new SecurityRequirement().addList("jwtAuth"));
    }


}