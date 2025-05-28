package com.sprint.mission.discodeit.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserPrincipal;
import com.sprint.mission.discodeit.filter.CustomUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.basic.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;
  private final ObjectMapper objectMapper;
  private final UserMapper userMapper;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
    // 1. 커스텀 로그인 필터 생성
    CustomUsernamePasswordAuthenticationFilter loginFilter = new CustomUsernamePasswordAuthenticationFilter(objectMapper);
    loginFilter.setAuthenticationManager(authManager);

    // 2. 로그인 성공 핸들러 - 로그인 성공 시 UserDto 응답 처리
    loginFilter.setAuthenticationSuccessHandler((req, res, auth) -> {

      User user = ((UserPrincipal) auth.getPrincipal()).getUser();
      UserDto userDto = userMapper.toDto(user);

      SecurityContext context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(auth);
      SecurityContextHolder.setContext(context);

      HttpSession session = req.getSession(true);
      session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

      res.setStatus(HttpServletResponse.SC_OK);
      res.setContentType("application/json");
      objectMapper.writeValue(res.getWriter(), userDto);
    });

    // 3. 로그인 실패 핸들러
    loginFilter.setAuthenticationFailureHandler((req, res, ex) -> {
      res.setStatus(HttpStatus.UNAUTHORIZED.value());
      res.setContentType("application/json");
      res.getWriter().write("{\"message\":\"로그인 실패: " + ex.getMessage() + "\"}");
    });

    // 4. Security 설정 구성
    return http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(
                "/api/users", "/api/auth/login", "/api/auth/logout"
            )
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/",                    // 루트
                "/index.html",          // CSR 진입점
                "/favicon.ico",         // 아이콘
                "/error",               // 오류 페이지
                "/assets/**",           // ✅ 정적 자원 경로 (React, Vite, etc.)
                "/css/**",
                "/js/**",
                "/images/**",
                "/api/auth/login",      // 로그인 API
                "/api/auth/csrf-token",
                "/api/auth/register",// CSRF 토큰 발급
                "/api/users"            // 회원가입
            ).permitAll()
            .requestMatchers("/api/channels/**").hasAnyRole("CHANNEL_MANAGER", "ADMIN")
            .requestMatchers("/api/auth/role").hasRole("ADMIN")
            .anyRequest().hasRole("USER")
        )
        .addFilterAt(loginFilter, CustomUsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(customUserDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
