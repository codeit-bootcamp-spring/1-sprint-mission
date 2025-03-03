package com.sprint.mission.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev") // dev profile에서만 swagger 사용
public class OpenApiConfiguration {

    private static final String API_VERSION = "v1";
    private static final String API_NAME = "Discodeit API 문서";
    private static final String API_DESCRIPTION = "Discodeit 프로젝트의 Swagger API 문서입니다";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_NAME)
                        .version(API_VERSION)
                        .description(API_DESCRIPTION));
    }
}
