package com.sprint.mission.discodeit.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.sprint.mission.discodeit.security.DiscodeitLoginFilter;
import com.sprint.mission.discodeit.security.DiscodeitLogoutFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
      DiscodeitLoginFilter discodeitLoginFilter, DiscodeitLogoutFilter discodeitLogoutFilter)
      throws Exception {
    CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    cookieCsrfTokenRepository.setCookieName("CSRF-TOKEN");
    cookieCsrfTokenRepository.setHeaderName("X-CSRF-TOKEN");

    return httpSecurity
        .authorizeHttpRequests(requests -> requests
            .requestMatchers("/api/auth/csrf-token", "/api/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers("/api/**").authenticated()
            .anyRequest().permitAll())
        .csrf(csrfConfig -> csrfConfig.disable()
//            .csrfTokenRepository(cookieCsrfTokenRepository)
//            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
//            .ignoringRequestMatchers("/api/auth/csrf-token")
        )
//        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
        .addFilterBefore(discodeitLoginFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(discodeitLogoutFilter, LogoutFilter.class)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(withDefaults())
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
