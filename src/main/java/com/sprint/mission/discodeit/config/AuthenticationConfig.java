package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.CustomRememberMeServices;
import com.sprint.mission.discodeit.security.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.handler.CustomAuthenticationFailureHandler;
import com.sprint.mission.discodeit.security.handler.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

// 인증 관련 Bean들

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    // AuthenticationManager Bean (AuthenticationManager 는 자동 Bean 등록이 안 됨)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
        throws Exception {
        return config.getAuthenticationManager();
    }

    // 로그인 필터 Bean
    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter(
        AuthenticationManager authManager,
        RememberMeServices rememberMeServices,
        CustomAuthenticationSuccessHandler successHandler,
        CustomAuthenticationFailureHandler failureHandler,
        SecurityContextRepository securityContextRepository
    ) {
        JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter(
            authManager);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setSecurityContextRepository(securityContextRepository);
        filter.setRememberMeServices(rememberMeServices);
        return filter;
    }

    // 인증 정보 저장소 (세션)
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // 유저 정보 조회
        provider.setPasswordEncoder(passwordEncoder); // 비밀번호 검증
        return provider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RememberMeServices rememberMeServices(
        UserDetailsService userDetailsService,
        PersistentTokenRepository tokenRepository,
        @Value("${discodeit.security.remember-me.key}") String key
    ) {
        return new CustomRememberMeServices(key, userDetailsService, tokenRepository);
    }

}
