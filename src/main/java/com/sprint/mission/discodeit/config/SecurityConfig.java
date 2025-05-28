package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.User.Role;
import com.sprint.mission.discodeit.security.CsrfCookieFilter;
import com.sprint.mission.discodeit.security.CustomSessionInformationExpiredStrategy;
import com.sprint.mission.discodeit.security.DiscodeitLoginFilter;
import com.sprint.mission.discodeit.security.DiscodeitLogoutFilter;
import com.sprint.mission.discodeit.security.LoginFailureHandler;
import com.sprint.mission.discodeit.security.LoginSuccessHandler;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.session.ConcurrentSessionFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, ObjectMapper objectMapper,
      DiscodeitLoginFilter discodeitLoginFilter, DiscodeitLogoutFilter discodeitLogoutFilter) throws Exception {
    return httpSecurity
        .authorizeHttpRequests(requests -> requests
            .requestMatchers(
                SecurityMatchers.NON_API,
                SecurityMatchers.GET_CSRF_TOKEN,
                SecurityMatchers.SIGN_UP
            ).permitAll()
            .requestMatchers(HttpMethod.PUT, "/api/auth/role").hasRole(Role.ADMIN.name())
            .requestMatchers(HttpMethod.POST, "/api/channels/public").hasRole(Role.CHANNEL_MANAGER.name())
            .requestMatchers(HttpMethod.PATCH, "/api/channels/*").hasRole(Role.CHANNEL_MANAGER.name())
            .requestMatchers(HttpMethod.DELETE, "/api/channels/*").hasRole(Role.CHANNEL_MANAGER.name())
            .anyRequest().hasRole(Role.USER.name())
        )
        .csrf(csrfConfig -> csrfConfig
            .csrfTokenRepository(csrfTokenRepository())
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            .ignoringRequestMatchers(SecurityMatchers.LOGOUT)
        )
        .sessionManagement(sessionConfig -> sessionConfig
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
        )
        .securityContext(context -> context.securityContextRepository(securityContextRepository()))
        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
        .addFilterAt(discodeitLoginFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterAt(discodeitLogoutFilter, LogoutFilter.class)
        .addFilter(new ConcurrentSessionFilter(sessionRegistry(), new CustomSessionInformationExpiredStrategy(objectMapper)))
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
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
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role(Role.ADMIN.name())
        .implies(Role.USER.name(), Role.CHANNEL_MANAGER.name())

        .role(Role.CHANNEL_MANAGER.name())
        .implies(Role.USER.name())

        .build();
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public CsrfTokenRepository csrfTokenRepository() {
    CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    cookieCsrfTokenRepository.setCookieName("CSRF-TOKEN");
    cookieCsrfTokenRepository.setHeaderName("X-CSRF-TOKEN");
    return cookieCsrfTokenRepository;
  }

  @Bean
  public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(sessionRegistry());
  }

  @Bean
  public DiscodeitLoginFilter discodeitLoginFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
    DiscodeitLoginFilter filter = new DiscodeitLoginFilter(objectMapper);

    filter.setRequiresAuthenticationRequestMatcher(SecurityMatchers.LOGIN);
    filter.setAuthenticationManager(authenticationManager);
    filter.setSecurityContextRepository(securityContextRepository());
    filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(objectMapper));
    filter.setAuthenticationFailureHandler(new LoginFailureHandler(objectMapper));
    filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());

    return filter;
  }

  @Bean
  public DiscodeitLogoutFilter discodeitLogoutFilter() {
    DiscodeitLogoutFilter filter = new DiscodeitLogoutFilter(
        new HttpStatusReturningLogoutSuccessHandler(),
        new SecurityContextLogoutHandler()
    );
    filter.setLogoutRequestMatcher(SecurityMatchers.LOGOUT);

    return filter;
  }
}
