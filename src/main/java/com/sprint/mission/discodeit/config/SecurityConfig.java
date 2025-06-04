package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.exception.CustomAuthenticationEntryPoint;
import com.sprint.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtService jwtTokenProvider;
  private final UserDetailsService userDetailsService;

  @Value("${discodeit.security.csrf.cookie.secure}")
  private boolean secure;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http,
      JwtAuthenticationFilter jwtAuthenticationFilter)
      throws Exception {

    CookieCsrfTokenRepository repo = CookieCsrfTokenRepository.withHttpOnlyFalse();
    repo.setCookiePath("/");
    repo.setCookieCustomizer(builder -> builder.sameSite("Lax").secure(secure));

    XorCsrfTokenRequestAttributeHandler handler = new XorCsrfTokenRequestAttributeHandler();
    handler.setCsrfRequestAttributeName("_csrf");

    http
        // JWT 사용 시 CSRF 불필요
        .csrf(AbstractHttpConfigurer::disable)
        // 세션 정책: 세션 사용 안함
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // JWT 필터 등록
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class)
        // URL 별 인증 규칙 설정
        .authorizeHttpRequests(auth -> auth
            // 정적 리소스 / 기타 토큰 발급
            .requestMatchers(
                "/",
                "/index.html",
                "/login",
                "/api/auth/login",
                "/api/auth/logout",
                "/api/auth/csrf-token",
                "/api/users",
                "/error",
                "/css/**",
                "/js/**",
                "/images/**",
                "/favicon.ico",
                "/static/**",
                "/assets/**"
            ).permitAll()
            // Swagger - API 문서 공개 경로
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            // 전체적/공용/크게 변하지 않을 요소들만 이곳에 표현
            .requestMatchers("/api/admin/**").hasRole("ADMIN")  // 관리자 전용 API 제한
            .requestMatchers("/api/**").hasRole("USER")         // 전체적으로 USER Role 필요
            // 그 외 경로는 모두 인증 필요
            .anyRequest().authenticated()
        );
    return http.build();
  }

  // 비밀번호 암호화
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // 비밀번호 암호화 시 Bcrypt 해시 사용
  }

  // JWT 필터
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(
      GrantedAuthoritiesMapper authoritiesMapper) {
    return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService, authoritiesMapper);
  }

  // 인증 실패 처리 - 인증되지 않은 요청에 대한 응답 처리
  @Bean
  public AuthenticationEntryPoint customAuthenticationEntryPoint() {
    return new CustomAuthenticationEntryPoint();
  }

  // 인증 로직 관리
  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity httpSecurity,
      AuthenticationProvider provider)
      throws Exception {
    return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(provider)
        .build();
  }

  // 사용자 인증 수행
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService); // 사용자 정보 로딩
    provider.setPasswordEncoder(passwordEncoder());     // 비밀번호 인코딩 전략 (비밀번호 암호화)
    return provider;
  }

  // 권한에 계층 부여
  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role("ADMIN").implies("CHANNEL_MANAGER")
        .role("CHANNEL_MANAGER").implies("USER")
        .build();
  }

  @Bean
  public GrantedAuthoritiesMapper authoritiesMapper(RoleHierarchy roleHierarchy) {
    return new RoleHierarchyAuthoritiesMapper(roleHierarchy);
  }

}
