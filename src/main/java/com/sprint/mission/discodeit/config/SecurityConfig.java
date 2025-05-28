package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.CustomLoginFilter;
import com.sprint.mission.discodeit.security.CustomLogoutFilter;
import com.sprint.mission.discodeit.security.CustomSessionInformationExpiredStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices(
            @Value("${security.remember-me.key}") String key,
            @Value("${security.remember-me.token-validity-seconds}") int tokenValiditySeconds,
            UserDetailsService userDetailsService,
            DataSource dataSource
    ) {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        tokenRepository.setCreateTableOnStartup(false); // 테이블 이미 존재하면 false

        PersistentTokenBasedRememberMeServices rememberMeServices =
                new PersistentTokenBasedRememberMeServices(key, userDetailsService, tokenRepository);
        rememberMeServices.setTokenValiditySeconds(tokenValiditySeconds);
        return rememberMeServices;
    }


    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
        "ROLE_ADMIN > ROLE_CHANNEL_MANAGER\n" +
        "ROLE_CHANNEL_MANAGER > ROLE_USER"
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    //AuthenticationManager 수동 등록 loginForm 사용안해서 이렇게 해야함
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            AuthenticationProvider authenticationProvider
    ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider)
                .build();
    }

    @Bean
    public CustomLoginFilter customLoginFilter(
            AuthenticationManager authenticationManager,
            SessionAuthenticationStrategy sessionStrategy,
            AuthenticationSuccessHandler successHandler,
            ObjectMapper objectMapper,
            PersistentTokenBasedRememberMeServices rememberMeServices
    ) {
        CustomLoginFilter filter = new CustomLoginFilter(
                authenticationManager,
                sessionStrategy,
                successHandler,
                objectMapper
        );
        filter.setRememberMeServices(rememberMeServices);
        return filter;
    }

    @Bean
    public CustomLogoutFilter customLogoutFilter(PersistentTokenRepository tokenRepository) {
        return new CustomLogoutFilter(tokenRepository);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CustomLoginFilter customLoginFilter,
            CustomLogoutFilter customLogoutFilter,
            SessionRegistry sessionRegistry,
            ObjectMapper objectMapper,
            PersistentTokenBasedRememberMeServices rememberMeServices
    ) throws Exception {
        http
                //.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/auth/logout"))
                .logout(logout -> logout.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // 회원가입만 허용
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll()
                        .requestMatchers(new NegatedRequestMatcher(new AntPathRequestMatcher("/api/**"))).permitAll()
                        .requestMatchers(
                                "/api/auth/login?**",
                                "/api/auth/csrf-token" // csrf 토큰 발급 api
                        ).permitAll()
                        .anyRequest().hasRole("USER")
                )
                .formLogin(form -> form.disable())
                .rememberMe(r -> r
                        .rememberMeServices(rememberMeServices)
                )
                .sessionManagement(session -> session
                        .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId) //세션고정보호
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .sessionRegistry(sessionRegistry)
                )
                .addFilter(
                        new ConcurrentSessionFilter(sessionRegistry,
                                new CustomSessionInformationExpiredStrategy(objectMapper))
                )
                .addFilterBefore(customLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customLogoutFilter, LogoutFilter.class);
        return http.build();
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        repo.setCreateTableOnStartup(false); // 이미 테이블 만들었으면 false
        return repo;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
        // 동시 로그인 제어 전략
        ConcurrentSessionControlAuthenticationStrategy concurrentStrategy =
                new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);

        concurrentStrategy.setMaximumSessions(1); // 최대 1명 로그인
        concurrentStrategy.setExceptionIfMaximumExceeded(false); // 새 로그인 허용, 기존 세션 만료

        // 세션 등록 전략
        RegisterSessionAuthenticationStrategy registerStrategy =
                new RegisterSessionAuthenticationStrategy(sessionRegistry);

        // 두 전략을 결합해서 적용
        return new CompositeSessionAuthenticationStrategy(List.of(
                concurrentStrategy,
                registerStrategy
        ));
    }
}


