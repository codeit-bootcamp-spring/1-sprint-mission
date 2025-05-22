package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .logout(logout -> logout.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        "/",
                                        "/index.html",
                                        "/favicon.ico",  // 파비콘
                                        "/assets/**", // JS/CSS 번들
                                        "/static/**", // 정적 리소스
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/actuator/**",
                                        "/api/auth/csrf-token", // csrf 토큰 발급 api
                                        "/api/users" // 회원가입 api 허용
                                ).permitAll()
                                .anyRequest().authenticated()
                        )
                .formLogin(form -> form.disable())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

}
