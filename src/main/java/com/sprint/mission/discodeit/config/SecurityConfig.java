package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.CustomAuthenticationEntryPoint;
import com.sprint.mission.discodeit.security.CustomUserDetailService;
import com.sprint.mission.discodeit.security.evaluator.CustomPermissionEvaluator;
import com.sprint.mission.discodeit.security.handler.CustomLogoutHandler;
import com.sprint.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.security.jwt.JwtProperties;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    private final CustomPermissionEvaluator customPermissionEvaluator;
    private final JwtService jwtService;
    private final CustomUserDetailService customUserDetailService;

    @Bean
    SecurityFilterChain chain(
        HttpSecurity http,
        SecurityContextRepository securityContextRepository,
        CustomLogoutHandler customLogoutHandler,
        AuthenticationEntryPoint authenticationEntryPoint) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)  // JWT 사용시 CSRF 불필요
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함

            .securityContext(
                context -> context.securityContextRepository(securityContextRepository))

            .authorizeHttpRequests(this::configureAuthorization)

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint))

            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 인가 정책
    private void configureAuthorization(
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth

            // 허용
            .requestMatchers("/", "/index.html", "/error").permitAll()
            .requestMatchers("/.well-known/**", "/favicon.ico").permitAll()
            .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()  // 로그인 관련은 모두 허용
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger 허용
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

            // 채널 관리
            .requestMatchers(HttpMethod.DELETE, "/api/channels").hasRole("CHANNEL_MANAGER")
            .requestMatchers("/api/channels/public/**").hasRole("CHANNEL_MANAGER")

            // 유저 권한 수정
            .requestMatchers("/api/auth/role").hasRole("ADMIN")

            // 기본 인증
            .requestMatchers("/api/**").hasRole("USER")

            // anyRequest
            .anyRequest().permitAll();
    }

    // csrf
//    private CookieCsrfTokenRepository csrfTokenRepository() {
//        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
//        repository.setCookieName("XSRF-TOKEN");
//        repository.setHeaderName("X-XSRF-TOKEN");
//        repository.setCookiePath("/");
//        return repository;
//    }

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailService); // 사용자 정보 제공
        authProvider.setPasswordEncoder(passwordEncoder); // 비밀번호 인코딩 방식 지정
        return new ProviderManager(authProvider);
    }

//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider(
//        UserDetailsService userDetailsService,
//        PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService); // 유저 정보 조회
//        provider.setPasswordEncoder(passwordEncoder); // 비밀번호 검증
//        return provider;
//    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    // 토큰 저장소 설정
    @Bean
    public PersistentTokenRepository tokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        repository.setCreateTableOnStartup(false);
        return repository;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
        RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(customPermissionEvaluator);
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, customUserDetailService);
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new CustomAuthenticationEntryPoint(objectMapper);
    }
}
