package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/auth/csrf-token",
                "/api/users"
            ).permitAll()
            .requestMatchers("/api/**").authenticated() // /api/를 포함할 경우 인증 필요
            .anyRequest().permitAll())  // 그 외 모든 url 요청에 대해 인증 X
        .httpBasic(Customizer.withDefaults()) // HTTP Basic 로그인 허용
        .formLogin(AbstractHttpConfigurer::disable)  // security가 기본 제공하는 form 기반 로그인 비활성화
        .logout(AbstractHttpConfigurer::disable); // logout 필터 제거

    return http.build();  // 필터 체인 만들어짐
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // BCrypt 암호화 사용
  }
}