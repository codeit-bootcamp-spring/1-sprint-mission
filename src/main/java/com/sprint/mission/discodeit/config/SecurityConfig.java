package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.User.Role;
import com.sprint.mission.discodeit.security.CsrfCookieFilter;
import com.sprint.mission.discodeit.security.DiscodeitLoginFilter;
import com.sprint.mission.discodeit.security.DiscodeitLogoutFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
      DiscodeitLoginFilter discodeitLoginFilter, DiscodeitLogoutFilter discodeitLogoutFilter,
      DefaultWebSecurityExpressionHandler webExpr
      )
      throws Exception {
    CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    cookieCsrfTokenRepository.setCookieName("CSRF-TOKEN");
    cookieCsrfTokenRepository.setHeaderName("X-CSRF-TOKEN");

    return httpSecurity
        .authorizeHttpRequests(requests -> requests
            .requestMatchers("/api/auth/csrf-token", "/api/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers(HttpMethod.PUT, "/api/auth/role").hasAuthority(Role.ROLE_ADMIN.getAuthority())
            .requestMatchers(HttpMethod.POST, "/api/channels/public").hasAuthority(Role.ROLE_CHANNEL_MANAGER.getAuthority())
            .requestMatchers(HttpMethod.PATCH, "/api/channels/*").hasAuthority(Role.ROLE_CHANNEL_MANAGER.getAuthority())
            .requestMatchers(HttpMethod.DELETE, "/api/channels/*").hasAuthority(Role.ROLE_CHANNEL_MANAGER.getAuthority())
            .requestMatchers("/api/**").hasAuthority(Role.ROLE_USER.getAuthority())
            .anyRequest().permitAll()
        )
        .csrf(csrfConfig -> csrfConfig
            .csrfTokenRepository(cookieCsrfTokenRepository)
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            .ignoringRequestMatchers("/api/auth/csrf-token", "/api/auth/logout")
            .ignoringRequestMatchers(new AntPathRequestMatcher("/api/users", HttpMethod.POST.name()))
        )
        .sessionManagement(sessionConfig -> sessionConfig
            .sessionConcurrency(concurrency -> concurrency
                .sessionRegistry(sessionRegistry())
            )
        )
        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
        .addFilterBefore(discodeitLoginFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(discodeitLogoutFilter, LogoutFilter.class)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
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

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy("""
        ROLE_ADMIN > ROLE_CHANNEL_MANAGER
        ROLE_CHANNEL_MANAGER > ROLE_USER
        """);
  }

  @Bean
  public DefaultWebSecurityExpressionHandler webExpr(RoleHierarchy rh) {
    DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
    handler.setRoleHierarchy(rh);
    return handler;
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public RegisterSessionAuthenticationStrategy regStrategy(SessionRegistry registry) {
    return new RegisterSessionAuthenticationStrategy(registry);
  }
}
