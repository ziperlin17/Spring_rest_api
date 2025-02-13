package ru.kpfu.itis.lifeTrack.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                description = "OpenApi documentation for Eternal Timeline api",
                title = "Eternal Timeline",
                version = "0.0.1"
        ),
        servers = {
                 @Server(
                         description = "Local ENV",
                         url = "http://localhost:8080/api"
                 ),
                @Server(
                        description = "PROD ENV",
                        url = "http://eternitytimeline:8080/api"
                )
        },
        security = {
            @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Bearer Authentication token. You can get it after Login/Signup",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
