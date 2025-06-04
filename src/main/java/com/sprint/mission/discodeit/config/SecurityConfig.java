package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.auth.DiscodeitUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.basic.UserOnlineStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserMapper userMapper;

  @Value("${app.jwt.refresh-token-expiration}")
  private long refreshTokenExpiration;

  @Bean
  public SecurityFilterChain chain(
      HttpSecurity http,
      AuthenticationManager manager,
      RememberMeServices rememberMeServices,
      UserOnlineStatusService statusService,
      JwtService jwtService,
      JwtAuthenticationFilter jwtAuthenticationFilter
  ) throws Exception {

    CsrfTokenRequestAttributeHandler plain =
        new CsrfTokenRequestAttributeHandler();
    plain.setCsrfRequestAttributeName("_csrf"); // test 필요

    DiscodeitUsernamePasswordAuthenticationFilter filter = new DiscodeitUsernamePasswordAuthenticationFilter(
        userMapper, sessionRegistry(), rememberMeServices, statusService, jwtService,
        refreshTokenExpiration);

    filter.setAuthenticationManager(manager);
    filter.setRequiresAuthenticationRequestMatcher(
        new AntPathRequestMatcher("/api/auth/login", "POST")
    );

    http
        .rememberMe(rm -> rm.rememberMeServices(rememberMeServices))
//        .sessionManagement(
//            session -> session
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .sessionManagement(session -> session.sessionFixation().migrateSession())
        .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository()).csrfTokenRequestHandler(plain)
            .ignoringRequestMatchers("/api/users", "/api/auth/check-session"))
        .addFilterBefore(jwtAuthenticationFilter,
            DiscodeitUsernamePasswordAuthenticationFilter.class)
        .addFilterAt(filter, DiscodeitUsernamePasswordAuthenticationFilter.class)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/assets/**",
                "/favicon.ico",
                "/index.html",
                "/api/auth/login",
                "/api/auth/csrf-token",
                "/api/users",
                "/",
                "/api/auth/check-session"
            ).permitAll()
            .requestMatchers(HttpMethod.GET, "/api/channels").hasRole("USER")
            .requestMatchers(HttpMethod.POST, "/api/channels/private").hasRole("USER")
            .requestMatchers("/api/auth/role").hasRole("ADMIN")
            .requestMatchers("/api/channels/**").hasRole("CHANNEL_MANAGER")
            .anyRequest().hasRole("USER")
        );

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public CsrfTokenRepository csrfTokenRepository() {
    CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    repository.setCookieName("XSRF-TOKEN");
    repository.setHeaderName("X-XSRF-TOKEN");
    repository.setCookiePath("/");
    return repository;
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
      RoleHierarchy roleHierarchy) {
    DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
    handler.setRoleHierarchy(roleHierarchy);
    return handler;
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
}

