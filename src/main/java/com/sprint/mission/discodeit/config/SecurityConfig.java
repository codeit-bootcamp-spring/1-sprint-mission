package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.JsonAuthenticationFailureHandler;
import com.sprint.mission.discodeit.security.JsonAuthenticationFilter;
import com.sprint.mission.discodeit.security.JsonAuthenticationSuccessHandler;
import com.sprint.mission.discodeit.security.JsonLogoutFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final JsonAuthenticationSuccessHandler successHandler;
    private final JsonAuthenticationFailureHandler failureHandler;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(
                    "/api/auth/csrf-token",
                    "/api/users",
                    "/api/auth/login",
                    "/api/auth/logout"
                )
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(10)
                .sessionRegistry(sessionRegistry)
            )
            .securityContext(context -> context
                .securityContextRepository(new HttpSessionSecurityContextRepository())
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html", "/static/**", "/favicon.ico").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html")
                .permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/auth/csrf-token", "/api/auth/login", "/api/users")
                .permitAll()
                .requestMatchers("/api/auth/role").hasRole("ADMIN")
                .requestMatchers("/api/channels/public")
                .access(hasAnyRole("CHANNEL_MANAGER", "ADMIN"))
                .requestMatchers("/api/channels/{channelId}").access((authentication, context) -> {
                    String method = context.getRequest().getMethod();
                    if ("PATCH".equals(method) || "DELETE".equals(method)) {
                        return hasAnyRole("CHANNEL_MANAGER", "ADMIN").check(authentication,
                            context);
                    }
                    return hasAnyRole("USER", "CHANNEL_MANAGER", "ADMIN").check(authentication,
                        context);
                })
                .requestMatchers("/api/**").hasAnyRole("USER", "CHANNEL_MANAGER", "ADMIN")
                .anyRequest().permitAll()
            )
            .logout(logout -> logout.disable())
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            )
            .addFilterBefore(jsonAuthenticationFilter(
                    authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(new JsonLogoutFilter("/api/auth/logout"),
                JsonAuthenticationFilter.class);

        SecurityFilterChain filterChain = http.build();
        log.info("Security Filter Chain configured with filters:");
        filterChain.getFilters().forEach(filter ->
            log.info("- {}", filter.getClass().getSimpleName())
        );

        return filterChain;
    }

    private AuthorizationManager<RequestAuthorizationContext> hasAnyRole(String... roles) {
        String expression = "hasAnyRole(" +
            String.join(",",
                java.util.Arrays.stream(roles).map(r -> "'" + r + "'").toArray(String[]::new)) +
            ")";
        return new WebExpressionAuthorizationManager(expression);
    }

    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter(
        AuthenticationManager authenticationManager) {
        JsonAuthenticationFilter filter = new JsonAuthenticationFilter(
            "/api/auth/login",
            authenticationManager,
            objectMapper
        );
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
        throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
    
}
