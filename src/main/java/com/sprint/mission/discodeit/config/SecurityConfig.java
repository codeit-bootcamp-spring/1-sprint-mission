package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.CustomAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain chain(HttpSecurity http,
      CustomAuthenticationFilter customAuthenticationFilter) throws Exception {

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
            .requestMatchers("/api/auth/csrf-token").permitAll()
            .requestMatchers("/api/**").authenticated()
            .anyRequest().permitAll())
        .addFilterAt(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
            .sessionRegistry(sessionRegistry())
        )
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
//        .csrf(csrf -> csrf
//            .csrfTokenRepository(csrfTokenRepository())
//        );
        .csrf(AbstractHttpConfigurer::disable);
    return http.build();
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
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public HttpSessionSecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public CustomAuthenticationFilter customAuthFilter(AuthenticationManager authenticationManager,
      HttpSessionSecurityContextRepository securityContextRepository,
      UserMapper userMapper, SessionRegistry sessionRegistry) {
    return new CustomAuthenticationFilter(authenticationManager, securityContextRepository,
        userMapper, sessionRegistry);
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
}
