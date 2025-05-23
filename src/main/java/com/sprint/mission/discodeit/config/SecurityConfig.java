package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.CustomAuthenticationEntryPoint;
import com.sprint.mission.discodeit.security.CustomLoginFilter;
import com.sprint.mission.discodeit.security.CustomLogoutFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;


@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint entryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
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
    public CustomLoginFilter customLoginFilter(AuthenticationManager authenticationManager) {
        return new CustomLoginFilter(authenticationManager);
    }

    @Bean
    public CustomLogoutFilter customLogoutFilter() {
        return new CustomLogoutFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager,
            CustomLoginFilter customLoginFilter,
            CustomLogoutFilter customLogoutFilter
    ) throws Exception {
        http
                //.csrf(csrf -> csrf.disable())
                //.cors(Customizer.withDefaults())
                .logout(logout -> logout.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(
                                        "/api/auth/login",
                                        "/api/auth/logout",
                                        "/api/auth/csrf-token", // csrf 토큰 발급 api
                                        "/api/users", // 회원가입 api 허용
                                        "/",
                                        "/index.html",
                                        "/favicon.ico",  // 파비콘
                                        "/assets/**", // JS/CSS 번들
                                        "/static/**", // 정적 리소스
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/actuator/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                        )
                .formLogin(form -> form.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(entryPoint)
                )

                .addFilterBefore(customLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customLogoutFilter, LogoutFilter.class);

        return http.build();
    }

}
