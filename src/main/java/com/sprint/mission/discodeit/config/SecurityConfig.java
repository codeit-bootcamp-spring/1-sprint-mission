package com.sprint.mission.discodeit.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.sprint.mission.discodeit.filter.CsrfCookieFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    cookieCsrfTokenRepository.setCookieName("CSRF-TOKEN");
    cookieCsrfTokenRepository.setHeaderName("X-CSRF-TOKEN");
    return httpSecurity
        .authorizeHttpRequests(requests -> requests
            .requestMatchers("/api/**").authenticated()
            .requestMatchers("/api/auth/csrf-token", "/api/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .anyRequest().permitAll())
        .csrf(csrfConfig -> csrfConfig
            .csrfTokenRepository(cookieCsrfTokenRepository)
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            .ignoringRequestMatchers("/api/auth/csrf-token")
        )
        .sessionManagement(sessionConfig -> sessionConfig
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(withDefaults())
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
