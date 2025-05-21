package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.security.CustomAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
      AuthenticationManager authManager, SecurityContextRepository contextRepository)
      throws Exception {

    CustomAuthenticationFilter customFilter = new CustomAuthenticationFilter(authManager);
    // 실패 시 JSON 메시지 반환
    customFilter.setAuthenticationFailureHandler((request, response, exception) -> {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"message\":\"아이디 또는 비밀번호가 올바르지 않습니다.\"}");
    });
    customFilter.setSecurityContextRepository(contextRepository);

    // 커스텀 필터 등록
    http.addFilterAt(customFilter, UsernamePasswordAuthenticationFilter.class);

    // 보안 설정
    http.authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/auth/csrf-token",
                "/api/users",
                "/api/auth/login"
            ).permitAll()
            .requestMatchers("/api/**").authenticated() // /api/를 포함할 경우 인증 필요
            .anyRequest().permitAll())  // 그 외 모든 url 요청에 대해 인증 X
        .httpBasic(AbstractHttpConfigurer::disable) // 로그인 팝업 없애기 위해 끔
        .formLogin(AbstractHttpConfigurer::disable)  // security가 기본 제공하는 form 기반 로그인 비활성화
        .logout(AbstractHttpConfigurer::disable); // logout 필터 제거

    return http.build();  // 필터 체인 만들어짐
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // BCrypt 암호화 사용
  }

  @Bean
  public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder) {

    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class).build();
  }
}