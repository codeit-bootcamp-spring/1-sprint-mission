package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain chain(HttpSecurity http) throws Exception {
    http
        .csrf(Customizer.withDefaults())
        .logout(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST,"/api/users").permitAll()
            .requestMatchers("/api/auth/csrf-token").permitAll()
            .requestMatchers("/", "/index.html", "/favicon.ico",
                "/css/**", "/js/**", "/images/**", "/static/**", "/error", "/assets/**"
            ).permitAll()
            .requestMatchers("/api/**").authenticated()
            .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder encoder) {
    UserDetails user = User.withUsername("api-user")
        .password(encoder.encode("p@ssw0rd"))
        .roles("USER")
        .build();
    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
}
