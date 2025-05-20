package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addServersItem(localServerInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Discodeit API 문서")  // API 제목
                .description("Discodeit 프로젝트의 Swagger API 문서입니다.")   // API에 대한 설명
                .version("2.0.0");  // API 버전
    }

    private Server localServerInfo() {
        return new Server()
                .url("http://localhost:8080")
                .description("로컬 서버");
    }
}
