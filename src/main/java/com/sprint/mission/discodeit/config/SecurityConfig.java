package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.AuthenticationFilter;
import com.sprint.mission.discodeit.security.CustomUserDetailsService;
import com.sprint.mission.discodeit.security.JsonAuthenticationFailureHandler;
import com.sprint.mission.discodeit.security.JsonAuthenticationSuccessHandler;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CompositeLogoutHandler;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final ObjectMapper objectMapper;
  private final UserMapper userMapper;
  private final CustomUserDetailsService userDetailsService;

  @Bean
  SecurityFilterChain chain(HttpSecurity http,
      PersistentTokenRepository repository) throws Exception {
    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName("_csrf");

    CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    tokenRepository.setCookieName("CSRF-TOKEN");
    tokenRepository.setHeaderName("X-CSRF-TOKEN");

    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(tokenRepository)
            .csrfTokenRequestHandler(requestHandler)
            .ignoringRequestMatchers("/api/auth/logout"))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/images/**", "/js/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/api/auth/csrf-token").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/api/auth/me").authenticated()
            .requestMatchers("/api/**").hasRole(Role.USER.name())
            .anyRequest().authenticated())
        .securityContext(securityContext -> securityContext.securityContextRepository(
            securityContextRepository()))
        .httpBasic(Customizer.withDefaults())
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .sessionFixation(SessionFixationConfigurer::changeSessionId)
            .invalidSessionUrl("/login?invalid")
            .sessionConcurrency(concurrency -> concurrency
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/login?expired")))
        .rememberMe(r -> r
            .rememberMeParameter("remember")
            .tokenRepository(repository)
            .tokenValiditySeconds(60 * 60 * 24 * 21)
            .key("persistentKey123!")
            .userDetailsService(userDetailsService));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(authenticationProvider());
  }

  @Bean
  public AuthenticationFilter authenticationFilter() {
    AuthenticationFilter filter = new AuthenticationFilter(authenticationManager(), objectMapper);
    filter.setAuthenticationSuccessHandler(
        new JsonAuthenticationSuccessHandler(objectMapper, userMapper));
    filter.setAuthenticationFailureHandler(new JsonAuthenticationFailureHandler(objectMapper));
    filter.setSecurityContextRepository(securityContextRepository());
    return filter;
  }

  @Bean
  public LogoutHandler logoutHandler() {
    CompositeLogoutHandler logoutHandler = new CompositeLogoutHandler(
        new SecurityContextLogoutHandler(),
        new CookieClearingLogoutHandler("JSESSIONID", "CSRF-TOKEN", "remember-me")
    );
    return logoutHandler;
  }

  @Bean
  RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy(Role.getHierarchy());
  }

  @Bean
  public PersistentTokenRepository tokenRepository(DataSource dataSource) {
    JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
    repository.setDataSource(dataSource);

    repository.setCreateTableOnStartup(false);
    return repository;
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }
}