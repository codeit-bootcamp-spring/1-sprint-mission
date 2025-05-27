package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.JsonAuthenticationFailureHandler;
import com.sprint.mission.discodeit.security.JsonAuthenticationFilter;
import com.sprint.mission.discodeit.security.JsonAuthenticationSuccessHandler;
import com.sprint.mission.discodeit.security.JsonLogoutFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final JsonAuthenticationSuccessHandler successHandler;
    private final JsonAuthenticationFailureHandler failureHandler;

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
                .requestMatchers("/api/auth/csrf-token").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/users").permitAll()
                .requestMatchers("/api/**").authenticated()
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
}
