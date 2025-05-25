package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain chain(
      HttpSecurity http,
      CookieCsrfTokenRepository cookieCsrfTokenRepository,
      AuthenticationManager authenticationManager,
      ObjectMapper objectMapper
  )
      throws Exception {
    CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
    handler.setCsrfRequestAttributeName("_csrf");

    http
        .authenticationManager(authenticationManager)
        .authorizeHttpRequests(request ->
            request
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/static/**",
                    "/error",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/actuator/**",
                    "/assets/**",
                    "/static/index.html",
                    "/static/favicon.ico").permitAll()
                .requestMatchers("/api/auth/csrf-token").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/auth/role").hasRole("ADMIN")
                .requestMatchers(
                    "/api/channels/public",
                    "/api/channels/{channelId}").hasRole("CHANNEL_MANAGER")
                .anyRequest().hasRole("USER"))
        .csrf(csrf ->
            csrf
                .csrfTokenRequestHandler(handler)
                .csrfTokenRepository(cookieCsrfTokenRepository)
                .ignoringRequestMatchers(new AntPathRequestMatcher("/api/users", "POST"))
                .ignoringRequestMatchers(new AntPathRequestMatcher("/api/auth/logout", "POST"))
        )
        .with(new JsonUsernamePasswordAuthenticationFilter.Configurer(objectMapper), Customizer.withDefaults())
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .logout(logout -> logout.disable());

    return http.build();
  }

  @Bean
  CookieCsrfTokenRepository cookieCsrfTokenRepository() {

    CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

    cookieCsrfTokenRepository.setCookieName("CSRF-TOKEN");
    cookieCsrfTokenRepository.setHeaderName("X-CSRF-TOKEN");

    return cookieCsrfTokenRepository;
  }
 
  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder
  ) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      List<AuthenticationProvider> authenticationProviders) {
    return new ProviderManager(authenticationProviders);
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy("""
        ROLE_ADMIN > ROLE_CHANNEL_MANAGER
        ROLE_CHANNEL_MANAGER > ROLE_USER
    """);
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }



}
