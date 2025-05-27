package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.security.CustomLogoutFilter;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity //Spring Security 활성화
@Slf4j
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        ObjectMapper objectMapper,
        AuthenticationManager authenticationManager
    ) throws Exception {
        http
            .authorizeHttpRequests(authorize->authorize
                .requestMatchers(
                    //API가 아닌 다른 요청은 인증을 수행하지 않음
                    SecurityMatchers.NON_API,
                    //CSRF 토큰 발급 API 인증 제외
                    SecurityMatchers.GET_CSRF_TOKEN,
                    //회원가입 API 인증 제외
                    SecurityMatchers.SIGN_UP
                ).permitAll()
                .anyRequest().authenticated()
            )
            //로그아웃 시엔 CSRF 토큰 인증을 하지 않도록 처리
            .csrf(csrf->csrf.ignoringRequestMatchers(SecurityMatchers.LOGOUT))
            //로그아웃 관련 필터를 제외
            .logout(AbstractHttpConfigurer::disable)
            //Username~ 가 주입되던 필터 자리에 JsonUser~ 가 포함
            .addFilterAt(
                JsonUsernamePasswordAuthenticationFilter.createDefault(objectMapper,authenticationManager),
                UsernamePasswordAuthenticationFilter.class
            )
            .addFilterAt(
                CustomLogoutFilter.createDefault(),
                LogoutFilter.class
            )
        ;
        return http.build();
    }
    //BCryptPasswordEncoder 구현체로 PasswordEncoder 를 Bean으로 정의
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
            .implies(Role.USER.name(),Role.CHANNEL_MANAGER.name())
            .role(Role.CHANNEL_MANAGER.name())
            .implies(Role.USER.name())
            .build();
    }
    //기본 등록되는 필터 목록을 확인하기 위해 임시 등록 Bean
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
}
