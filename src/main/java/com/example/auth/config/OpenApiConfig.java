package com.example.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiConfig.class);

    @Bean
    public OpenAPI customOpenAPI() {
        logger.info("Creating OpenAPI configuration");

        return new OpenAPI()
                .info(new Info()
                        .title("Theatre Booking API")
                        .version("1.0")
                        .description("API documentation for Theatre Booking System")
                        .contact(new Contact()
                                .name("Theatre Support")
                                .email("support@theatre.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}