package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.filter.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.filter.LogoutFilter;
import com.sprint.mission.discodeit.security.LoginFailureHandler;
import com.sprint.mission.discodeit.security.LoginSuccessHandler;
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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final LoginSuccessHandler loginSuccessHandler;
  private final LoginFailureHandler loginFailureHandler;

  @Bean
  public SecurityFilterChain chain(HttpSecurity http, AuthenticationManager authManager,
      LogoutFilter logoutFilter) throws Exception {
    JsonUsernamePasswordAuthenticationFilter authFilter = new JsonUsernamePasswordAuthenticationFilter(
        authManager);
    authFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
    authFilter.setAuthenticationFailureHandler(loginFailureHandler);

    http
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/api/auth/login","/api/auth/logout"))
        .addFilterAt(authFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(logoutFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers("/api/auth/login", "/api/auth/csrf-token",
                "/api/auth/me", "/api/auth/logout").permitAll()
            .requestMatchers("/", "/index.html", "/favicon.ico",
                "/css/**", "/js/**", "/images/**", "/static/**", "/error", "/assets/**"
            ).permitAll()
            .requestMatchers(
                "api/channels/public", "/api/channels/public/**").hasRole("CHANNEL_MANAGER")
            .requestMatchers("/api/auth/role").hasRole("ADMIN")
            .requestMatchers("/api/**").authenticated()
            .anyRequest().hasRole("USER"));
    return http.build();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy("""
        ROLE_ADMIN > ROLE_CHANNEL_MANAGER
        ROLE_CHANNEL_MANAGER > ROLE_USER
        """);
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(encoder());
    return new ProviderManager(provider);
  }
}
