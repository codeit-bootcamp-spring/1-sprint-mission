package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 1. discodeit은 로그아웃 페이지를 CSR로 처리하므로 LogoutFilter 제외
     * <p>
     * 2. HttpOnly를 false로 설정하여 JavaScript에서 쿠키에 접근 가능하게 함
     */
    @Bean
    SecurityFilterChain chain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/csrf-token").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll())
            .logout(logout -> logout.logoutUrl("/disabled"));
        return http.build();
    }

}
