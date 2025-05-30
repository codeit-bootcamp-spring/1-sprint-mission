package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    SecurityFilterChain filterChain = http
        .logout(logout -> logout.disable())

        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
        )

        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/csrf-token").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // 회원가입

            .requestMatchers(request -> !request.getRequestURI().contains("/api/")).permitAll()

            .anyRequest().authenticated()
        )

        .httpBasic(Customizer.withDefaults())
        .build();

    System.out.println("=== Spring Security Filter Chain ===");
    filterChain.getFilters().forEach(filter ->
        System.out.println("- " + filter.getClass().getSimpleName()));
    System.out.println("====================================");

    return filterChain;
  }
}