package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.CustomAuthenticationFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain chain(HttpSecurity http,
      CustomAuthenticationFilter customAuthenticationFilter,
      PersistentTokenRepository tokenRepository) throws Exception {

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers("/api/auth/login",
                "/api/auth/csrf-token")
            .permitAll()
            .requestMatchers("/api/channels/public").hasRole("CHANNEL_MANAGER")
            .requestMatchers(HttpMethod.PATCH, "/api/channels/**").hasRole("CHANNEL_MANAGER")
            .requestMatchers(HttpMethod.DELETE, "/api/channels/**").hasRole("CHANNEL_MANAGER")
            .requestMatchers("/api/auth/role").hasAnyRole("ADMIN")
            .anyRequest().hasRole("USER")
        )
        .addFilterAt(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .sessionFixation().changeSessionId()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
            .sessionRegistry(sessionRegistry())
        )
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler((request, response, authentication) -> {
              Cookie cookie = new Cookie("JSESSIONID", "");
              cookie.setMaxAge(0);
              cookie.setPath("/");
              cookie.setHttpOnly(true);
              response.addCookie(cookie);

              response.setStatus(HttpServletResponse.SC_OK);
            })
            .invalidateHttpSession(true)
            .clearAuthentication(true)
        )
        .rememberMe(r -> r
            .rememberMeParameter("remember-me")
            .tokenRepository(tokenRepository)
            .tokenValiditySeconds(60 * 60 * 24 * 21)
            .key("mySuperSecretKey123!")
        )
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
//        .csrf(csrf -> csrf
//              .ignoringRequestMatchers("/api/auth/logout")
//            .csrfTokenRepository(csrfTokenRepository())
//        );
        .csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        .requestMatchers("/",
            "/static/**",
            "/assets/**",
            "/index.html",
            "/favicon.ico");
  }

  @Bean
  public CsrfTokenRepository csrfTokenRepository() {
    CookieCsrfTokenRepository repo = CookieCsrfTokenRepository.withHttpOnlyFalse();
    repo.setCookieName("CSRF-TOKEN");
    repo.setHeaderName("X-CSRF-TOKEN");
    return repo;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public HttpSessionSecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public CustomAuthenticationFilter customAuthFilter(AuthenticationManager authenticationManager,
      HttpSessionSecurityContextRepository securityContextRepository,
      UserMapper userMapper, CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy) {

    return new CustomAuthenticationFilter(
        authenticationManager, securityContextRepository, userMapper,
        sessionAuthenticationStrategy);
  }
  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy(
        "ROLE_ADMIN > ROLE_CHANNEL_MANAGER\n" +
            "ROLE_CHANNEL_MANAGER > ROLE_USER"
    );
  }

  @Bean
  public CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy() {
    ConcurrentSessionControlAuthenticationStrategy concurrentStrategy =
        new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
    concurrentStrategy.setMaximumSessions(1);
    concurrentStrategy.setExceptionIfMaximumExceeded(false);

    return new CompositeSessionAuthenticationStrategy(Arrays.asList(
        concurrentStrategy,
        new SessionFixationProtectionStrategy(),
        new RegisterSessionAuthenticationStrategy(sessionRegistry())
    ));
  }

  @Bean
  public PersistentTokenRepository tokenRepository(DataSource dataSource) {
    JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
    repository.setDataSource(dataSource);
    return repository;
  }

  @Bean
  public RememberMeServices rememberMeServices(UserDetailsService userDetailsService,
      PersistentTokenRepository tokenRepository) {
    PersistentTokenBasedRememberMeServices services =
        new PersistentTokenBasedRememberMeServices("remember-me-key", userDetailsService, tokenRepository);
    services.setTokenValiditySeconds(60 * 60 * 24 * 21);
    services.setAlwaysRemember(false);
    return services;
  }

}
