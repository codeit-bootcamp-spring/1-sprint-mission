package com.sprint.mission.discodeit.global.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/**").permitAll()
				.requestMatchers(
					"/swagger-ui/**",
					"/v3/api-docs/**",
					"/swagger-ui.html",
					//여기를 수정하니까 되네....
					"/api-docs/**"
				).permitAll()  // Swagger UI 관련 경로 허용
				.anyRequest().permitAll()
			)
			//Todo 원래 httpbasic이었는데 fromlogin으로 일시적인 수정
			.formLogin(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://localhost:8080"));
		config.addAllowedHeader("*");
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
