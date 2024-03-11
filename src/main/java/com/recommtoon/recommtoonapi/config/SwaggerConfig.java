package com.recommtoon.recommtoonapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Recommtoon").version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("Access_Token", new SecurityScheme()
                                .type(Type.APIKEY)
                                .in(In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("Access_Token"));
    }
}
