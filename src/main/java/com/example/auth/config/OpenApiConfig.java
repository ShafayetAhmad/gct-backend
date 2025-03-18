package com.example.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

        private static final Logger logger = LoggerFactory.getLogger(OpenApiConfig.class);

        @Bean
        public OpenAPI theatreAPI() {
                logger.info("Creating OpenAPI configuration");

                Server devServer = new Server()
                                .url("http://localhost:8080")
                                .description("Development server");

                Contact contact = new Contact()
                                .name("Greenwich Community Theatre")
                                .email("support@greenwichtheatre.com")
                                .url("https://greenwichtheatre.com");

                Info info = new Info()
                                .title("Greenwich Community Theatre API")
                                .version("1.0.0")
                                .contact(contact)
                                .description("API for managing theatre performances, bookings, and reviews")
                                .license(new License()
                                                .name("MIT License")
                                                .url("https://opensource.org/licenses/MIT"));

                return new OpenAPI()
                                .info(info)
                                .servers(List.of(devServer));
        }
}