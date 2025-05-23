package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.filter.CustomLoginFilter;
import com.sprint.mission.discodeit.security.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.security.handler.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer.SessionFixationConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Slf4j
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final LoginSuccessHandler loginSuccessHandler;
  private final LoginFailureHandler loginFailureHandler;
  private final ObjectMapper objectMapper;
  private final UserDetailsService userDetailsService;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy(
        "ROLE_ADMIN > ROLE_CHANNEL_MANAGER\n" + "ROLE_CHANNEL_MANAGER > ROLE_USER"
    );
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(daoAuthenticationProvider());
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public CustomLoginFilter customLoginFilter(AuthenticationManager authenticationManager,
      SecurityContextRepository securityContextRepository) {
    CustomLoginFilter filter = new CustomLoginFilter(objectMapper);
    filter.setFilterProcessesUrl("/api/auth/login");
    filter.setAuthenticationManager(authenticationManager);
    filter.setAuthenticationSuccessHandler(loginSuccessHandler);
    filter.setAuthenticationFailureHandler(loginFailureHandler);
    filter.setSecurityContextRepository(securityContextRepository);
    return filter;
  }

  @Bean
  public SecurityFilterChain chain(
      HttpSecurity http,
      CustomLoginFilter customLoginFilter,
      SecurityContextRepository securityContextRepository,
      SessionRegistry sessionRegistry) throws Exception {

    // formLogin 비활성화
    http.formLogin(AbstractHttpConfigurer::disable)
        // .csrf(AbstractHttpConfigurer::disable)
        // HTTP Basic 인증 비활성화
        .httpBasic(AbstractHttpConfigurer::disable)
        // 세션 관리 설정
        .sessionManagement(session -> session
                .sessionFixation(SessionFixationConfigurer::changeSessionId
                )
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry)
            //.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        )
        // SecurityContext 저장소 설정
        .securityContext(context -> context
            .securityContextRepository(securityContextRepository)
        )
        // CSRF 무시
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(
                "/h2-console/**",
                "/api/auth/logout")
        )
        .headers(headers -> headers
            .frameOptions(
                FrameOptionsConfig::sameOrigin)) // X-Frame-Options 를 SAMEORIGIN 설정 (H2 콘솔 프레임 허용)
        // URL 별 접근 권한 설정
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/",
                "/h2-console/**",
                "/api/auth/login",
                "/api/auth/csrf-token",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/actuator/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers("/api/auth/role").hasRole("ADMIN")
            .requestMatchers("/api/channels/public").hasRole("CHANNEL_MANAGER")
            .requestMatchers(HttpMethod.PATCH, "/api/channels/{channelId}")
            .hasRole("CHANNEL_MANAGER")
            .requestMatchers(HttpMethod.DELETE, "/api/channels/{channelId}")
            .hasRole("CHANNEL_MANAGER")
            .requestMatchers("/api/**").hasRole("USER")
            .anyRequest().authenticated()
        )
        // 로그아웃
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .invalidateHttpSession(true) // 세션 무효화 처리
            .clearAuthentication(true) // securityContext 초기화
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
        );

    http.addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}
