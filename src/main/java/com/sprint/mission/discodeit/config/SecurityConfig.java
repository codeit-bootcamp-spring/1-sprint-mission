package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.JsonLogoutFilter;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.LoginStatusCheckFilter;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
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
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserDetailsService userDetailsService;

  @Value("${discodeit.cookie.persistent-key}")
  private String cookiePersistentKey;

  @Bean
  public PersistentTokenRepository tokenRepository(DataSource dataSource) {
    JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
    repository.setDataSource(dataSource);

    repository.setCreateTableOnStartup(false);

    return repository;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http,
      AuthenticationManager authenticationManager, PersistentTokenRepository tokenRepository,
      RememberMeServices rememberMeServices, SessionRegistry sessionRegistry,
      SessionAuthenticationStrategy sessionAuthenticationStrategy,
      LoginStatusCheckFilter loginStatusCheckFilter)
      throws Exception {

    CookieCsrfTokenRepository repo = CookieCsrfTokenRepository.withHttpOnlyFalse();
    repo.setCookiePath("/");
    repo.setHeaderName("X-CSRF-TOKEN");  // 프론트에서 이 이름으로 헤더 보냄
    repo.setCookieName("CSRF-TOKEN");
    repo.setCookieCustomizer(builder -> builder.sameSite("None").secure(true));

    XorCsrfTokenRequestAttributeHandler handler = new XorCsrfTokenRequestAttributeHandler();
    handler.setCsrfRequestAttributeName("_csrf");

    http
        // CSRF 설정: 쿠키 연동 시 CookiesCsrfTokenRepository 사용
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/api/users")
            .csrfTokenRepository(repo)
            .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()::handle)
        )
        // 인증 정보를 서버 세션에 저장
        .securityContext(context -> context
            .securityContextRepository(new HttpSessionSecurityContextRepository()))
        // 세션 정책: 인증 성공 시 세션 생성
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .sessionFixation().changeSessionId()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
            .sessionRegistry(sessionRegistry)
        )
        // URL 별 인증 규칙 설정
        .authorizeHttpRequests(auth -> auth
            // CSRF 토큰 발급 API는 인증하지 않음
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
            ).permitAll()            // 전체적/공용/크게 변하지 않을 요소들만 이곳에 표현
            .requestMatchers("/api/admin/**").hasRole("ADMIN")  // 관리자 전용 API 제한
            .requestMatchers("/api/**").hasRole("USER")         // 전체적으로 USER Role 필요
            .anyRequest().authenticated()
        )
        // 인증 공급자 등록 (UserDetailsService + PasswordEncoder 기반)
        .authenticationProvider(authenticationProvider())
        // 커스텀 로그인 필터 등록
        .addFilterBefore(
            usernamePasswordAuthenticationFilter(authenticationManager, rememberMeServices,
                sessionAuthenticationStrategy),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(
            loginStatusCheckFilter, UsernamePasswordAuthenticationFilter.class
        )
        // 기본 세팅 해제
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .httpBasic(HttpBasicConfigurer::disable)
        .rememberMe(r -> r
            .rememberMeCookieName("PERSIST")
            .rememberMeParameter("remember")
            .tokenRepository(tokenRepository)
            .tokenValiditySeconds(60 * 60 * 24 * 21)
            .key(cookiePersistentKey)
        );
    return http.build();
  }

  // 비밀번호 암호화
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // 비밀번호 암호화 시 Bcrypt 해시 사용
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

  // 로그인 필터 등록
  @Bean
  public JsonUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(
      AuthenticationManager authenticationManager, RememberMeServices rememberMeServices,
      SessionAuthenticationStrategy sessionAuthenticationStrategy) {
    return new JsonUsernamePasswordAuthenticationFilter(authenticationManager,
        rememberMeServices, sessionAuthenticationStrategy);
  }

  // 로그아웃 필터 등록
  @Bean
  public JsonLogoutFilter jsonLogoutFilter(PersistentTokenRepository tokenRepository,
      SessionRegistry sessionRegistry) {
    return new JsonLogoutFilter(tokenRepository, sessionRegistry);
  }

  @Bean
  public LoginStatusCheckFilter loginStatusCheckFilter() {
    return new LoginStatusCheckFilter();
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

  // RememberME
  @Bean
  public RememberMeServices rememberMeServices(PersistentTokenRepository tokenRepository) {
    return new PersistentTokenBasedRememberMeServices(
        cookiePersistentKey,
        userDetailsService,
        tokenRepository
    );
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
    return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
  }

  @Bean
  public SessionAuthenticationStrategy sessionAuthenticationStrategy(
      SessionRegistry sessionRegistry) {
    List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<>();
    delegateStrategies.add(new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry));
    delegateStrategies.add(new SessionFixationProtectionStrategy());
    delegateStrategies.add(new RegisterSessionAuthenticationStrategy(sessionRegistry));
    return new CompositeSessionAuthenticationStrategy(delegateStrategies);
  }

}
