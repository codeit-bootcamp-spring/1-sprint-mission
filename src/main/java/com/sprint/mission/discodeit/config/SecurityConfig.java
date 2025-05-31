package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.security.filter.CustomLoginFilter;
import com.sprint.mission.discodeit.security.handler.CustomLogoutHandler;
import com.sprint.mission.discodeit.security.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.security.handler.LoginSuccessHandler;
import com.sprint.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Slf4j
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final LoginSuccessHandler loginSuccessHandler;
  private final LoginFailureHandler loginFailureHandler;
  private final ObjectMapper objectMapper;
  private final UserDetailsService userDetailsService;
  //
  private final JwtService jwtService;

  @Bean
  public CookieCsrfTokenRepository cookieCsrfTokenRepository() {
    CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse(); // httpOnly 설정 false
    repository.setCookieName("XSRF-TOKEN"); // 프론트가 읽을 쿠키 이름
    repository.setHeaderName("X-XSRF-TOKEN"); // 서버가 읽을 쿠키의 헤더 이름
    repository.setCookiePath("/"); // csrf 쿠키 적용
    return repository;
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy(
        "ROLE_ADMIN > ROLE_CHANNEL_MANAGER\n" + "ROLE_CHANNEL_MANAGER > ROLE_USER"
    );
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(daoAuthenticationProvider());
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() { // 다음 단계에서 필터 구현
    return new JwtAuthenticationFilter(jwtService, userDetailsService);
  }

//  @Bean
//  public SecurityContextRepository securityContextRepository() {
//    return new HttpSessionSecurityContextRepository();
//  }

//  @Bean
//  public SessionAuthenticationStrategy sessionAuthenticationStrategy( // 인증 성공시 수행
//      SessionRegistry sessionRegistry) {
//
//    // 하단 HttpSecurity 의 sessionManagement 설정과 통일
//    // 동시 세션 제어 전략 설정
//    ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy =
//        new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
//    concurrentSessionControlAuthenticationStrategy.setMaximumSessions(1);
//
//    // 세션 고정 보호 전략
//    SessionFixationProtectionStrategy sessionFixationProtectionStrategy =
//        new SessionFixationProtectionStrategy();
//
//    // 세션 레지스트리 등록
//    RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy =
//        new RegisterSessionAuthenticationStrategy(sessionRegistry);
//
//    return new CompositeSessionAuthenticationStrategy(
//        Arrays.asList(
//            concurrentSessionControlAuthenticationStrategy,
//            sessionFixationProtectionStrategy,
//            registerSessionAuthenticationStrategy
//        )
//    );
//  }

//  @Bean
//  public PersistentTokenRepository tokenRepository(DataSource dataSource) {
//    JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
//    repository.setDataSource(dataSource);
//
//    repository.setCreateTableOnStartup(false);
//
//    return repository;
//  }

  @Bean
  public SecurityFilterChain chain(
      HttpSecurity http,
      CookieCsrfTokenRepository cookieCsrfTokenRepository,
      JwtAuthenticationFilter jwtAuthenticationFilter)
      throws Exception {

    // formLogin 비활성화
    http.formLogin(AbstractHttpConfigurer::disable)
        // .csrf(AbstractHttpConfigurer::disable)
        .csrf(csrf -> csrf
            .csrfTokenRepository(cookieCsrfTokenRepository)
            .ignoringRequestMatchers(
                "/h2-console/**",
                "/api/auth/logout")
        )
        // HTTP Basic 인증 비활성화
        .httpBasic(AbstractHttpConfigurer::disable)
        // 세션 관리 설정
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // URL 별 접근 권한 설정
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/",
                "/index.html",
                "/assets/**",
                "/favicon.ico",
                "/h2-console/**",
                "/api/auth/login",
                "/api/auth/csrf-token",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/actuator/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers("/api/auth/role").hasRole("ADMIN")
            .requestMatchers("/api/channels/public").hasRole("CHANNEL_MANAGER")
            .requestMatchers(HttpMethod.PATCH, "/api/channels/{channelId}")
            .hasRole("CHANNEL_MANAGER")
            .requestMatchers(HttpMethod.DELETE, "/api/channels/{channelId}")
            .hasRole("CHANNEL_MANAGER")
            .requestMatchers("/api/**").hasRole("USER")
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        // X-Frame-Options 를 SAMEORIGIN 설정 (H2 콘솔 프레임 허용)
        .headers(headers -> headers
            .frameOptions(
                FrameOptionsConfig::sameOrigin));
    // 로그아웃
//        .logout(logout -> logout
//            .logoutUrl("/api/auth/logout")
//            .invalidateHttpSession(true) // 세션 무효화 처리
//            .clearAuthentication(true) // securityContext 초기화
//            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
//            .deleteCookies("remember-me-cookie", "JSESSIONID")
//        );

//    http
//        .rememberMe(r -> r
//            .rememberMeParameter("remember-me")
//            .rememberMeCookieName("remember-me-cookie")
//            .tokenRepository(tokenRepository)
//            .tokenValiditySeconds(60 * 60 * 24 * 21)
//            .userDetailsService(userDetailsService)
//            .key("my!secret!key0cr!")
//        );

    return http.build();
  }

}
