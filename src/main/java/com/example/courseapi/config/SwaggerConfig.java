package com.example.courseapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger OpenAPI Configuration
 */
@Configuration
@RequiredArgsConstructor
@OpenAPIDefinition(
        info = @Info(
                title = "${application.name}",
                version = "1.0",
                description = "API for ${application.name}",
                license = @License(name = "Apache 2.0", url = "${application.url}"),
                contact = @Contact(name = "Vasyl Petrashchuk", email = "rockeman2013@gmail.com")
        ),
        security = @SecurityRequirement(name = "JWT Bearer authentication"),
        servers = {
                @Server(
                        description = "Current server",
                        url = "${application.url}"
                )
        }
)
@SecurityScheme(
        name = "JWT Bearer authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {}
