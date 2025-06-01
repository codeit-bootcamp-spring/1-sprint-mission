package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import com.sprint.mission.discodeit.security.filter.LoginAuthenticationFilter;
import com.sprint.mission.discodeit.security.filter.LogoutAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      ObjectMapper objectMapper,
      AuthenticationManager authenticationManager,
      AuthenticationSuccessHandler successHandler,
      AuthenticationFailureHandler failureHandler) throws Exception {
    // CSRF 설정
    CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    tokenRepository.setCookieName("CSRF-TOKEN");

    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName("_csrf");

    LoginAuthenticationFilter loginFilter = new LoginAuthenticationFilter(
        authenticationManager, objectMapper, successHandler, failureHandler
    );

    http
        // LogoutFilter 제외
        .logout(AbstractHttpConfigurer::disable)
        // CSRF 설정
        .csrf(csrf -> csrf.ignoringRequestMatchers(SecurityMatchers.LOGOUT))
        .logout(AbstractHttpConfigurer::disable)
        // 요청 인증 설정
        .authorizeHttpRequests(authorize -> authorize
            // CSRF 토큰 발급 API는 인증 제외
            .requestMatchers(HttpMethod.GET, "/api/auth/csrf-token").permitAll()
            // 회원가입 API는 인증 제외
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            // /api/를 포함하지 않는 모든 URL은 인증 제외 (정적 리소스, swagger, actuator 등)
            .requestMatchers(new AntPathRequestMatcher("/**", "GET"),
                request -> !request.getRequestURI().contains("/api/"))
            .permitAll()
            // 그 외 모든 요청은 인증 필요
            .anyRequest().authenticated()
        )

        .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAt(LogoutAuthenticationFilter.createDefault(), LogoutFilter.class)
        .exceptionHandling(exceptionHandling ->
            exceptionHandling
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                  accessDeniedException.printStackTrace();
                  response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF token mismatch");
                })
        );

    SecurityFilterChain filterChain = http.build();

    // 빌드 후에 필터 목록 로깅
    logFilterList(filterChain);

    return filterChain;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // 알기쉬운 비밀번호 제한
  @Bean
  public CompromisedPasswordChecker compromisedPasswordChecker(){
    return new HaveIBeenPwnedRestApiPasswordChecker();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  // 개발 환경에서 필터 목록 로깅을 위한 메서드
  @Profile("dev")
  private void logFilterList(SecurityFilterChain filterChain) {
    try {
      var filters = filterChain.getFilters();
      log.info("===== Security Filter List =====");
      for (int i = 0; i < filters.size(); i++) {
        log.info("{}: {}", i + 1, filters.get(i).getClass().getSimpleName());
      }
      log.info("===============================");
    } catch (Exception e) {
      log.error("필터 목록 로깅 중 오류 발생", e);
    }
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder,
      RoleHierarchy roleHierarchy
  ) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    provider.setAuthoritiesMapper(new RoleHierarchyAuthoritiesMapper(roleHierarchy));
    return provider;
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role(Role.ADMIN.name())
        .implies(Role.USER.name(), Role.CHANNEL_MANAGER.name())

        .role(Role.CHANNEL_MANAGER.name())
        .implies(Role.USER.name())

        .build();
  }
  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public SessionAuthenticationStrategy sessionAuthenticationStrategy(
      SessionRegistry sessionRegistry) {
    return new RegisterSessionAuthenticationStrategy(
        sessionRegistry);
  }
}
