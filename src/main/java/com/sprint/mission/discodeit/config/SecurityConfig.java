package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.security.CustomLoginFailureHandler;
import com.sprint.mission.discodeit.security.CustomLoginSuccessHandler;
import com.sprint.mission.discodeit.security.CustomPermissionEvaluator;
import com.sprint.mission.discodeit.security.CustomSessionInformationExpiredStrategy;
import com.sprint.mission.discodeit.security.filter.CustomLogoutFilter;
import com.sprint.mission.discodeit.security.filter.JsonUsernamePasswordAuthenticationFilter;
import com.sprint.mission.discodeit.security.filter.JwtAuthFilter;

import com.sprint.mission.discodeit.security.SecurityMatchers;
import com.sprint.mission.discodeit.security.SessionRegistryLogoutHandler;
import com.sprint.mission.discodeit.security.jwt.JwtBlacklist;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;

import java.util.stream.IntStream;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
      CustomPermissionEvaluator permissionEvaluator) {
    DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
    handler.setPermissionEvaluator(permissionEvaluator);
    handler.setRoleHierarchy(roleHierarchy());
    return handler;
  }

  //비밀번호 암호화를 위한 빈
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role(Role.ADMIN.name())
        .implies(Role.USER.name(), Role.CHANNEL_MANAGER.name())

        .role(Role.CHANNEL_MANAGER.name())
        .implies(Role.USER.name())

        .build();
  }

  //특정 유형의 인증 처리를 담당하는 인터페이스
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


  //인증 처리를 총괄하는 인터페이스
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
  //책임:
  //- 인증 요청(Authentication 객체) 수신
  //- 적절한 AuthenticationProvider 선택
  //- 인증 결과 반환
  //
  //특징:
  //- 여러 AuthenticationProvider 관리
  //- 인증 성공할 때까지 여러 Provider 시도


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

  @Bean
  public SecurityFilterChain chain(
      HttpSecurity http,
      JwtTokenProvider jwtTokenProvider,
      ObjectMapper objectMapper,
      DaoAuthenticationProvider daoAuthenticationProvider,
      JwtService jwtService,
      JwtBlacklist jwtBlacklist)
      throws Exception {

    http
        // authenticationProvider(daoAuthenticationProvider)로 명시적으로 등록하는 이유
        // : roleHierarchy 때문!
        .authenticationProvider(daoAuthenticationProvider)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                SecurityMatchers.NON_API,
                SecurityMatchers.GET_CSRF_TOKEN,
                SecurityMatchers.SIGN_UP,
                SecurityMatchers.ME
            ).permitAll()
            .anyRequest().hasRole(Role.USER.name())
        )

        .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화

        .formLogin(AbstractHttpConfigurer::disable)//디스코드잇은 CSR 방식이기 때문에 formLogin은 사용하지 않는다.

        // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        // 앞에 추가함을 통해 Spring Security 보다 JWT 검증이 먼저 이루어짐
        .addFilterBefore(new JwtAuthFilter(jwtTokenProvider, jwtBlacklist),
            UsernamePasswordAuthenticationFilter.class)

        // 로그아웃 필터 구현
        .logout(logout ->
            logout
                .logoutRequestMatcher(SecurityMatchers.LOGOUT)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
        )

        //.addFilter() - Spring Security 필터 체인에 커스텀 필터 추가
        //authenticationFilter() - 커스텀 인증 필터 생성 메서드
        //authenticationManager() - AuthenticationManager 객체 생성/반환
        //http.getSharedObject(AuthenticationConfiguration.class) - Spring이 관리하는 인증 설정 객체 가져오기

        .with(new JsonUsernamePasswordAuthenticationFilter.Configurer(objectMapper, jwtService),
            configure ->
                configure
                    .successHandler(new CustomLoginSuccessHandler(objectMapper, jwtService))
                    .failureHandler(new CustomLoginFailureHandler(objectMapper)))
        //with() + Configurer 방식
        //Configurer 패턴 사용: 필터 설정을 위한 전용 설정 클래스 활용 -> Configurer 내부에서 필터 생성과 설정을 모두 관리(캡슐화)
        //DSL 스타일: Spring Security의 fluent API 패턴을 따름
        //유연한 설정: 런타임에 동적으로 필터를 구성할 수 있다.

        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            .ignoringRequestMatchers(SecurityMatchers.LOGOUT)
        )

        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

    return http.build();
  }

}


