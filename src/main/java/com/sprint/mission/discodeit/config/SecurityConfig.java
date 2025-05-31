package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.User.Role;
import com.sprint.mission.discodeit.security.CsrfCookieFilter;
import com.sprint.mission.discodeit.security.CustomAccessDeniedHandler;
import com.sprint.mission.discodeit.security.CustomAuthenticationEntryPoint;
import com.sprint.mission.discodeit.security.CustomSessionInformationExpiredStrategy;
import com.sprint.mission.discodeit.security.DiscodeitLoginFilter.Configurer;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import com.sprint.mission.discodeit.security.SessionRegistryLogoutHandler;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final DataSource dataSource;
  private final ObjectMapper objectMapper;

  @Value("${security.remember-me.key}")
  private String key;
  @Value("${security.remember-me.token-validity-seconds}")
  private int tokenValiditySeconds;

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity httpSecurity,
      RememberMeServices rememberMeServices
  ) throws Exception {
    return httpSecurity
        .authorizeHttpRequests(requests -> requests
            .requestMatchers(
                SecurityMatchers.NON_API,
                SecurityMatchers.GET_CSRF_TOKEN,
                SecurityMatchers.SIGN_UP
            ).permitAll()
            .anyRequest().hasRole(Role.USER.name())
        )
        .csrf(csrfConfig -> csrfConfig
            .csrfTokenRepository(csrfTokenRepository())
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            .ignoringRequestMatchers(SecurityMatchers.LOGOUT)
        )
        .addFilterBefore(new CsrfCookieFilter(), CsrfFilter.class)
        .sessionManagement(sessionConfig -> sessionConfig
            .sessionFixation().migrateSession()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
            .sessionRegistry(sessionRegistry())
            .expiredSessionStrategy(new CustomSessionInformationExpiredStrategy(objectMapper))
        )
        .rememberMe(rememberMe -> rememberMe
            .rememberMeServices(rememberMeServices)
        )
        .with(new Configurer(objectMapper), Customizer.withDefaults())
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))
            .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper))
        )
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(logout -> logout
            .logoutRequestMatcher(SecurityMatchers.LOGOUT)
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .addLogoutHandler(new SessionRegistryLogoutHandler(sessionRegistry()))
        )
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
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
  public CsrfTokenRepository csrfTokenRepository() {
    return CookieCsrfTokenRepository.withHttpOnlyFalse();
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
    repo.setDataSource(dataSource);
    return repo;
  }

  @Bean
  public PersistentTokenBasedRememberMeServices rememberMeServices(
      UserDetailsService userDetailsService,
      PersistentTokenRepository persistentTokenRepository
  ) {
    PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
        key, userDetailsService, persistentTokenRepository);
    rememberMeServices.setTokenValiditySeconds(tokenValiditySeconds);

    return rememberMeServices;
  }
}
