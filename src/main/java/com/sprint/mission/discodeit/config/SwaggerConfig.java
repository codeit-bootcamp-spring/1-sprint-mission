package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .openapi("3.1.0")  // ✅ OpenAPI 버전 명시적으로 설정
        .info(new Info()
                .title("Discodeit API 문서")
                .description("Discodeit 프로젝트의 Swagger API 문서입니다.")
//            .version("1.0.0")  // API 문서 버전 (OpenAPI 버전 아님)
        )
        .servers(List.of(
            new Server().url("http://localhost:8080").description("로컬 서버")
        ));
  }
}
