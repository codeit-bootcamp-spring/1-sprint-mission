package com.sprint.mission.discodeit.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.components(new Components())
			.info(info());
	}

	private Info info() {
		return new Info()
			.title("코드잇 스프린트 미션 API")
			.description("Codeit Mission API reference for developers")
			.version("1.0");
	}
}
