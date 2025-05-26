package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    SecurityFilterChain filterChain = http
        // LogoutFilter 비활성화
        .logout(logout -> logout.disable())

        // CSRF 보호 활성화 (CSR 방식을 위한 설정)
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
        )

        // 인증 설정
        .authorizeHttpRequests(auth -> auth
            // 인증 불필요한 특정 엔드포인트들
            .requestMatchers("/api/auth/csrf-token").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // 회원가입

            // /api/를 포함하지 않는 모든 URL은 인증 제외 (정적 리소스, swagger, actuator 등)
            .requestMatchers(request -> !request.getRequestURI().contains("/api/")).permitAll()

            // 나머지 모든 요청(즉, /api/를 포함하는 요청)은 인증 필요
            .anyRequest().authenticated()
        )

        // 기본 로그인 방식 사용
        .httpBasic(Customizer.withDefaults())
        .build();

    // 필터 목록 디버깅 출력 (PR에 첨부할 내용)
    System.out.println("=== Spring Security Filter Chain ===");
    filterChain.getFilters().forEach(filter ->
        System.out.println("- " + filter.getClass().getSimpleName()));
    System.out.println("====================================");

    return filterChain;
  }
}