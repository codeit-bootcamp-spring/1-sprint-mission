package com.spirnt.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.security.CustomAuthenticationFailureHandler;
import com.spirnt.mission.discodeit.security.CustomAuthenticationFilter;
import com.spirnt.mission.discodeit.security.CustomAuthenticationSuccessHandler;
import com.spirnt.mission.discodeit.security.CustomUserDetailsService;
import com.spirnt.mission.discodeit.security.jwt.JwtAuthenticationFilter;
import com.spirnt.mission.discodeit.security.jwt.JwtLogoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain chain(HttpSecurity httpSecurity,
        CustomAuthenticationFilter customAuthenticationFilter,
        JwtAuthenticationFilter jwtAuthenticationFilter, JwtLogoutHandler jwtLogoutHandler)
        throws Exception {
        httpSecurity
            .authorizeHttpRequests(auth -> auth
                // 정적 리소스 요청 허용
                .requestMatchers("/static/**", "/assets/**", "/favicon.ico", "/index.html")
                .permitAll()
                .requestMatchers("/", "/error/**").permitAll()
                // Swagger 요청 허용
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html",
                    "/webjars/**").permitAll()
                // Actuator 요청 허용
                .requestMatchers("/actuator/**").permitAll()
                // CSRF 토큰 발급, 회원가입, 로그인 API 요청 허용
                .requestMatchers("/api/auth/csrf-token").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers("/api/binaryContents/**").permitAll()
                // 그 외 API는 최소 ROLE_USER 권한 필요
                .requestMatchers("/api/**").hasRole("USER")
                // 웹 소켓 연결 허용
                .requestMatchers("/ws/**").permitAll()
                // 그 외 인증 필요
                .anyRequest().authenticated())
            .csrf(csrf -> csrf
                // csrf 검증 제외
                .ignoringRequestMatchers("/api/users", "/api/auth/login",
                    "/api/auth/logout", "/ws/**")
                .csrfTokenRepository(customCsrfTokenRepository())
                // 요청으로부터 CSRF 토큰 가져오는 법: form data 또는 요청 헤더로부터 가져오는 CsrfTokenRequestAttributeHandler
                // -> 쿠키 기반 인증 환경에 적합
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                // 기본값인 CsrfAuthenticationStrategy는 HttpSessionCsrfTokenRepository용
                .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy())
            )
            // 커스텀 인증 필터 추가: UserAuthenticationFilter 대신 CustomAuthenticationFilter 사용
            .addFilterAt(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, CustomAuthenticationFilter.class)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .logout(logout -> logout
                .logoutRequestMatcher(
                    new AntPathRequestMatcher("/api/auth/logout"))  // POST /api/auth/logout으로 로그아웃
                .logoutSuccessUrl("/") // 세션 무효화 후 홈으로
                .deleteCookies("REFRESH-TOKEN")    // 쿠키 삭제
                .addLogoutHandler(jwtLogoutHandler) // 리프레시 토큰 무효화 핸들러
            );
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    CustomAuthenticationFilter customAuthenticationFilter(
        AuthenticationManager authenticationManager, ObjectMapper objectMapper,
        CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
        CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
            authenticationManager, objectMapper);
        // /api/auth/login 경로에만 필터 적용
        customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");
        // 핸들러 적용
        customAuthenticationFilter.setAuthenticationSuccessHandler(
            customAuthenticationSuccessHandler);
        customAuthenticationFilter.setAuthenticationFailureHandler(
            customAuthenticationFailureHandler);
        return customAuthenticationFilter;
    }


    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
            "ROLE_ADMIN > ROLE_CHANNEL_MANAGER\n" +
                "ROLE_CHANNEL_MANAGER > ROLE_USER"
        );
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
        RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

    // 쿠키, 헤더 이름 명시
    @Bean
    public CsrfTokenRepository customCsrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieName("XSRF-TOKEN");         // 쿠키 이름 지정
        repository.setHeaderName("X-XSRF-TOKEN");       // 클라이언트가 보낼 헤더 이름 지정
        return repository;
    }

}
