package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.UserDetailsAdapter;
import com.sprint.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Slf4j
@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, UserMapper userMapper,
      AuthenticationConfiguration configuration, JwtAuthenticationFilter jwtAuthenticationFilter,
      com.sprint.mission.discodeit.security.jwt.JwtService jwtService)
      throws Exception {
    JsonUsernamePasswordAuthenticationFilter loginFilter =
        new JsonUsernamePasswordAuthenticationFilter(authenticationManager(configuration));
    loginFilter.setAuthenticationSuccessHandler(successHandler(userMapper, jwtService));
    loginFilter.setAuthenticationFailureHandler(failureHandler());

    http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/api/auth/logout", "/api/auth/login", "/api/auth/refresh")
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
        )
        .authorizeHttpRequests(auth -> auth
            // 정적 리소스
            .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/**favicon.ico")
            .permitAll()
            .requestMatchers("/", "/index.html", "/assets/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**", "/static/**")
            .permitAll()

            // 관리자 전용
            .requestMatchers("/api/auth/role").hasRole("ADMIN")

            // 인증된 사용자
            .requestMatchers("/api/channels/**", "/api/readStatuses/**",
                "api/users/{userId}/userStatus", "/api/messages/**").permitAll()

            // 인증 불필요
            .requestMatchers("/api/auth/csrf-token", "/api/users", "/api/auth/login",
                "/api/auth/refresh").permitAll()

            // 로그아웃
            .requestMatchers("/api/auth/logout").authenticated()

            // Remember Me
            .requestMatchers("/api/auth/me").permitAll()

            .anyRequest().hasRole("USER")
        )
        .sessionManagement(session -> session.disable())
        .logout(logout -> logout.disable())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationSuccessHandler successHandler(UserMapper userMapper,
      com.sprint.mission.discodeit.security.jwt.JwtService jwtService) {
    return (request, response, authentication) -> {
      UserDetailsAdapter adapter = (UserDetailsAdapter) authentication.getPrincipal();
      User user = adapter.getUser();
      UserDto userDto = userMapper.toDto(user);

      log.info("로그인 성공, 유저 권한: " + authentication.getAuthorities());

      com.sprint.mission.discodeit.security.jwt.JwtService.JwtTokens tokens = jwtService.generateTokens(
          userDto);

      jakarta.servlet.http.Cookie refreshTokenCookie = new jakarta.servlet.http.Cookie(
          "refresh_token", tokens.refreshToken());
      refreshTokenCookie.setHttpOnly(false);
      refreshTokenCookie.setSecure(request.isSecure());
      refreshTokenCookie.setPath("/");
      refreshTokenCookie.setMaxAge(604800);
      response.addCookie(refreshTokenCookie);

      response.setContentType("application/json");
      com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
      objectMapper.writeValue(response.getWriter(), userDto);
    };
  }

  @Bean
  public AuthenticationFailureHandler failureHandler() {
    return (request, response, exception) -> {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"error\": \"로그인 실패\"}");
    };
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_CHANNEL_MANAGER > ROLE_USER");
    return roleHierarchy;
  }

  @Bean
  public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
      RoleHierarchy roleHierarchy) {
    DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setRoleHierarchy(roleHierarchy);
    return expressionHandler;
  }

  @Bean
  public CommandLineRunner initAdmin(UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    return args -> {
      if (!userRepository.existsByUsername("admin")) {
        User admin = User.builder()
            .username("admin")
            .email("admin@test.com")
            .password(passwordEncoder.encode("admin123"))
            .roles(Collections.singletonList("ROLE_ADMIN"))
            .build();
        userRepository.save(admin);
      }
    };
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
    JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
    repository.setDataSource(dataSource);
    return repository;
  }

  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepository) {
    return username -> {
      User user = userRepository.findByUsername(username)
          .orElseThrow(() -> UserNotFoundException.withUsername(username));

      return new UserDetailsAdapter(user);
    };
  }
}
