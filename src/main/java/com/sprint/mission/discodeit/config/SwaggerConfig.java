package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "JWT";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Discodeit API 문서")
                .description("Discodeit 프로젝트의 Swagger API 문서입니다.")
            )
            .servers(List.of(
                new Server().url("http://localhost:8080").description("로컬 서버")
            ))
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, createJWTTokenScheme()));
    }

    private SecurityScheme createJWTTokenScheme() {
        return new SecurityScheme()
            .name("Authorization")
            .type(Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(In.HEADER);
    }
}
