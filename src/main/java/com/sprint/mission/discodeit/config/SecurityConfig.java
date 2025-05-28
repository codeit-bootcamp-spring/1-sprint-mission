package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.CustomAuthenticationProvider;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.handler.CustomLogoutHandler;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    SecurityFilterChain chain(
        HttpSecurity http,
        CustomAuthenticationProvider authProvider, // DaoAuthenticationProvider provider,
        JsonUsernamePasswordAuthenticationFilter loginFilter,
        SecurityContextRepository securityContextRepository,
        CustomLogoutHandler customLogoutHandler,
        RememberMeServices rememberMeServices) throws Exception {

        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(csrfTokenRepository())
                .ignoringRequestMatchers("/api/auth/logout")
            )

            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .addLogoutHandler(customLogoutHandler)
                .logoutSuccessUrl("/")
            )

            .authenticationProvider(authProvider)

            .securityContext(
                context -> context.securityContextRepository(securityContextRepository))

            .authorizeHttpRequests(this::configureAuthorization)

            .rememberMe(r -> r
                .rememberMeServices(rememberMeServices)
            )

            .sessionManagement(s -> s
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation(sf -> sf.migrateSession()) // 세션 고정보호
                .maximumSessions(1) // 동시 로그인 제한
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/"))

            .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 인가 정책
    private void configureAuthorization(
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth

            // 허용
            .requestMatchers("/api/auth/csrf-token").permitAll()
            .requestMatchers("/api/auth/login").permitAll()
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
    private CookieCsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        repository.setCookieName("Csrf-Token");
        repository.setHeaderName("X-Csrf-Token");
        repository.setCookiePath("/");
        return repository;
    }

    // 토큰 저장소 설정
    @Bean
    public PersistentTokenRepository tokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
        repository.setCreateTableOnStartup(false);
        return repository;
    }

}
