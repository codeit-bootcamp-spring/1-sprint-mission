package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.filter.LoginAuthenticationFilter;
import com.sprint.mission.discodeit.filter.LogoutFilter;
import com.sprint.mission.discodeit.provider.LoginAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final LoginAuthenticationProvider loginAuthenticationProvider;

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(loginAuthenticationProvider)
        .build();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      AuthenticationManager authenticationManager
  ) throws Exception {
    LoginAuthenticationFilter loginFilter = new LoginAuthenticationFilter(authenticationManager);

    http.csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .logout(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                "/api/users",
                "/api/auth/csrf-token",
                "/api/auth/login"
            ).permitAll()
            .anyRequest().hasRole(Role.USER.name())
        )
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                "/api/channels/public",
                "/api/channels/userId"
            ).hasRole(Role.CHANNEL_MANAGER.name())
            .requestMatchers(HttpMethod.PATCH, "/api/channels/")
            .hasRole(Role.CHANNEL_MANAGER.name())
        )
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/actuator/**",
                "/api/auth/logout",
                "/css/**",
                "/js/**",
                "/images/**",
                "/",
                "/favicon.ico")
            .permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users")
            .permitAll()

            .anyRequest()
            .authenticated())
        .addFilterBefore(new LogoutFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
        .authenticationProvider(loginAuthenticationProvider)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable);
    return http.build();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy(
        "ROLE_ADMIN > ROLE_CHANNEL_MANAGER \n" +
            "ROLE_CHANNEL_MANAGER > ROLE_USER"
    );
  }
}
