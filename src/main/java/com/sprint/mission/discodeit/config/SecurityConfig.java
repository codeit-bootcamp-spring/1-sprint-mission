package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
      Exception {
    http
        .logout(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            // 인증 제외 경로: Swagger, 정적 리소스, actuator 등
            .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/actuator/**",
                "/api/auth/csrf-token",
                "/api/auth/login",
                "/css/**",
                "/js/**",
                "/images/**",
                "/",
                "/favicon.ico"
            )
            .permitAll()
            .requestMatchers(
                HttpMethod.POST,
                "/api/users")
            .permitAll()

            // 나머지 경로는 모두 인증 요구
            .anyRequest()
            .authenticated()
        )
        .formLogin(Customizer.withDefaults()) // 기본 form 로그인 사용
        .httpBasic(Customizer.withDefaults()) // HTTP Basic 인증도 활성화
        .csrf(AbstractHttpConfigurer::disable); // CSRF는 일단 개발용으로 비활성화

    return http.build();
  }

  @Bean
  UserDetailsService uds(PasswordEncoder enc) {
    UserDetails u = User.withUsername("api-user")
        .password(enc.encode("p@ssw0rd")) //BCrypt 암호화
        .roles("USER")
        .build();
    return new InMemoryUserDetailsManager(u);
  }

  @Bean
  PasswordEncoder enc() {
    return new BCryptPasswordEncoder();
  }


}
