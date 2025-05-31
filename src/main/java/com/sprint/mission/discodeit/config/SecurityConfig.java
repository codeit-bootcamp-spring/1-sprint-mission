package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.UserDetailsAdapter;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, UserMapper userMapper,
      PersistentTokenRepository persistentTokenRepository, UserDetailsService userDetailsService
      , AuthenticationConfiguration configuration)
      throws Exception {
    JsonUsernamePasswordAuthenticationFilter loginFilter =
        new JsonUsernamePasswordAuthenticationFilter(authenticationManager(configuration));
    loginFilter.setAuthenticationSuccessHandler(successHandler(userMapper));
    loginFilter.setAuthenticationFailureHandler(failureHandler());

    http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/api/auth/logout")
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
            .requestMatchers("/api/auth/csrf-token", "/api/users", "/api/auth/login").permitAll()

            // 로그아웃
            .requestMatchers("/api/auth/logout").authenticated()

            .anyRequest().hasRole("USER")
        )
        .securityContext(securityContext -> securityContext
            .securityContextRepository(new HttpSessionSecurityContextRepository())
        )
        .rememberMe(rememberMe -> rememberMe
            .tokenRepository(persistentTokenRepository)
            .tokenValiditySeconds(60 * 60 * 24 * 21)
            .userDetailsService(userDetailsService)
        )
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .deleteCookies("JSESSIONID", "remember-me")
            .addLogoutHandler((request, response, authentication) -> {
              if (authentication != null) {
                persistentTokenRepository.removeUserTokens(authentication.getName());
              }
            })
        )
        .sessionManagement(session -> session
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
            .expiredSessionStrategy(event -> {
              HttpServletResponse response = event.getResponse();
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.setContentType("application/json");
              response.getWriter().write("{\"error\": \"다른 기기에서 로그인되어 세션이 종료되었습니다.\"}");
            })
        )
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
  public AuthenticationSuccessHandler successHandler(UserMapper userMapper) {
    return (request, response, authentication) -> {
      UserDetailsAdapter adapter = (UserDetailsAdapter) authentication.getPrincipal();
      User user = adapter.getUser();
      UserDto userDto = userMapper.toDto(user);

      // 여기서 authentication.getAuthorities() 값을 확인합니다.
      System.out.println("로그인 성공! 유저 권한: " + authentication.getAuthorities());

      response.setContentType("application/json");
      new ObjectMapper().writeValue(response.getWriter(), userDto);
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
