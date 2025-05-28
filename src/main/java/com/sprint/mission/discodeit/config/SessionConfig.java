package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

@Configuration
public class SessionConfig {

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .sessionManagement(s -> s
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .sessionFixation(sf -> sf.migrateSession()) // 세션 고정보호
//                .maximumSessions(1) // 동시 로그인 제한
//                .maxSessionsPreventsLogin(false)
//                .expiredUrl("/"));
//
//        return http.build();
//    }
}
