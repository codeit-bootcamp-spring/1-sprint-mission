package com.sprint.mission.discodeit.configure;

import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.EnumSet;

@Configuration
@Slf4j
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      CustomLoginFilter loginFilter,
      JwtAuthenticationFilter jwtFilter) throws Exception {
//    http
//        .csrf(csrf -> csrf
//            .ignoringRequestMatchers("/api/auth/logout")
//            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//        )
//        .authorizeHttpRequests(auth -> auth
//            .requestMatchers("/api/auth/login", "/api/users", "/api/auth/csrf-token").permitAll()
//            .requestMatchers(HttpMethod.PUT, "/api/auth/role").hasRole("ADMIN")
//            .requestMatchers("/api/channels/**").hasAnyRole("CHANNEL_MANAGER", "ADMIN")
//            .anyRequest().hasRole("USER")
//        )
//        .sessionManagement(session -> session
//            .maximumSessions(1)
//            .sessionRegistry(sessionRegistry()))
//        .securityContext(securityContext ->
//            securityContext.securityContextRepository(new HttpSessionSecurityContextRepository()))
//        .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
//        .logout(AbstractHttpConfigurer::disable);
//
//    return http.build();
    CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    csrfTokenRepository.setCookieName("XSRF-TOKEN");
    csrfTokenRepository.setHeaderName("X-XSRF-TOKEN");

    http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/api/auth/logout")
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/login", "/api/users", "/api/auth/csrf-token").permitAll()
            .requestMatchers(HttpMethod.PUT, "/api/auth/role").hasRole("ADMIN")
            .requestMatchers("/api/channels/**").hasAnyRole("CHANNEL_MANAGER", "ADMIN")
            .anyRequest().hasRole("USER")
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(
            session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS))
        .logout(AbstractHttpConfigurer::disable);

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
      BCryptPasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return new ProviderManager(provider);
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy(
        "ROLE_ADMIN > ROLE_CHANNEL_MANAGER \n ROLE_CHANNEL_MANAGER > ROLE_USER");
    return roleHierarchy;
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
