package com.localride.server.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Use the JWT token obtained from the login endpoint in the format"
                + " `Bearer <token>` in the Authorization header for accessing secured endpoints."
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Local Ride Assistant API").version("1.0").description(
                        "API documentation for the Local Ride Assistant application."
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
}
