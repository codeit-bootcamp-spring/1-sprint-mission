package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        servers = @Server(
                url = "http://localhost:8080/",
                description = "로컬 서버"
        ),
        info = @Info(
                title = "Discodeit API 문서",
                description = "스프린트 미션 API 엔드포인트 명세서 입니다.",
                summary = "Code-it spring mission",
                version = "1.0",
                contact = @Contact(
                        name = "SB_1기_백재우",
                        email = "9712jw@gmail.com"
                )
        )
)
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("01. DiscodeIt API")
                .pathsToMatch("/api/**")
                .displayName("전체 API")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("02 Auth API")
                .pathsToMatch("/api/auth/**")
                .displayName("Auth API")
                .build();
    }

    @Bean
    public GroupedOpenApi binaryContentApi() {
        return GroupedOpenApi.builder()
                .group("03. BinaryContent API")
                .pathsToMatch("/api/binaryContents/**")
                .displayName("Binary Content API")
                .build();
    }

    @Bean
    public GroupedOpenApi channelApi() {
        return GroupedOpenApi.builder()
                .group("04. Channel API")
                .pathsToMatch("/api/channels/**")
                .displayName("Channel API")
                .build();
    }

    @Bean
    public GroupedOpenApi messageApi() {
        return GroupedOpenApi.builder()
                .group("05. Message API")
                .pathsToMatch("/api/messages/**")
                .displayName("Message API")
                .build();
    }

    @Bean
    public GroupedOpenApi readStatusApi() {
        return GroupedOpenApi.builder()
                .group("06. Read Status API")
                .pathsToMatch("/api/readStatuses/**")
                .displayName("Read Status API")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("07. User API")
                .pathsToMatch("/api/users/**")
                .displayName("User API")
                .build();
    }

}
