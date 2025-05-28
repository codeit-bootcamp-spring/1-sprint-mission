package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.security.CustomSessionInformationExpiredStrategy;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import com.sprint.mission.discodeit.security.SessionRegistryLogoutHandler;
import java.util.List;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  @Primary
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      ObjectMapper objectMapper,
      AuthenticationManager authenticationManager,
      SessionRegistry sessionRegistry,
      PersistentTokenBasedRememberMeServices rememberMeServices
  )
      throws Exception {
    http
        .authenticationManager(authenticationManager)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                SecurityMatchers.NON_API,
                SecurityMatchers.GET_CSRF_TOKEN,
                SecurityMatchers.SIGN_UP
            ).permitAll()
            .anyRequest().hasRole(Role.USER.name())
        )
        .csrf(csrf -> csrf.ignoringRequestMatchers(SecurityMatchers.LOGOUT))
        .logout(logout ->
            logout
                .logoutRequestMatcher(SecurityMatchers.LOGOUT)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .addLogoutHandler(new SessionRegistryLogoutHandler(sessionRegistry))
        )
        .with(new JsonUsernamePasswordAuthenticationFilter.Configurer(objectMapper),
            Customizer.withDefaults())
        .sessionManagement(session ->
            session
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry)
                .expiredSessionStrategy(new CustomSessionInformationExpiredStrategy(objectMapper))
        )
        .rememberMe(rememberMe -> rememberMe.rememberMeServices(rememberMeServices))
    ;

    return http.build();
  }

  @Bean
  public String debugFilterChain(SecurityFilterChain chain) {
    log.debug("Debug Filter Chain...");
    int filterSize = chain.getFilters().size();
    IntStream.range(0, filterSize)
        .forEach(idx -> {
          log.debug("[{}/{}] {}", idx + 1, filterSize, chain.getFilters().get(idx));
        });
    return "debugFilterChain";
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider(
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder,
      RoleHierarchy roleHierarchy
  ) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    provider.setAuthoritiesMapper(new RoleHierarchyAuthoritiesMapper(roleHierarchy));
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      List<AuthenticationProvider> authenticationProviders) {
    return new ProviderManager(authenticationProviders);
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
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public PersistentTokenBasedRememberMeServices rememberMeServices(
      @Value("${security.remember-me.key}") String key,
      @Value("${security.remember-me.token-validity-seconds}") int tokenValiditySeconds,
      UserDetailsService userDetailsService,
      DataSource dataSource
  ) {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);

    PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
        key, userDetailsService, tokenRepository);
    rememberMeServices.setTokenValiditySeconds(tokenValiditySeconds);

    return rememberMeServices;
  }
}
