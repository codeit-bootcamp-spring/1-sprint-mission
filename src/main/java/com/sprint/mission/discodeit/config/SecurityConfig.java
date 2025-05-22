package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.CustomUserDetails;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.filter.CustomAuthenticationFilter;
import com.sprint.mission.discodeit.filter.CustomLogoutFilter;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  //비밀번호 암호화를 위한 빈
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityContextRepository securityContextRepository() {
    //SecurityContextRepository 인터페이스의 기본 구현체
    return new HttpSessionSecurityContextRepository();

    //세션 저장 메커니즘
    //- 사용자의 인증 정보(SecurityContext)를 HttpSession에 저장
    //- 기본적으로 "SPRING_SECURITY_CONTEXT" 키로 세션에 저장
    //- 일반 스프링 세션 관리 메커니즘을 보안 목적으로 활용
    //
    //
    //동작 원리
    // - 사용자 로그인 성공 → SecurityContext 생성 → HttpSession에 저장
    // - 요청마다 세션에서 SecurityContext 로드 → 인증 상태 유지
    // - 로그아웃 → 세션에서 SecurityContext 제거

    //요청 → JSESSIONID 쿠키 → 세션 식별 → SecurityContext 로드 → 인증 정보 사용 → 필요시 SecurityContext 갱신 → 세션에 저장

  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy("""
        ROLE_ADMIN > ROLE_CHANNEL_MANAGER > ROLE_USER
        """
    );
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        return new CustomUserDetails(user);

      }
    };
  }


  //특정 유형의 인증 처리를 담당하는 인터페이스
  @Bean
  public AuthenticationProvider authenticationProvider() {
    //DaoAuthenticationProvider: DB 기반 인증
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService());
    authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
    return authenticationProvider;
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


  //역할: HTTP 요청에서 사용자명과 비밀번호를 추출하여 인증 처리
  @Bean
  public UsernamePasswordAuthenticationFilter authenticationFilter(
      AuthenticationManager authenticationManager) throws Exception {

    //UsernamePasswordAuthenticationFilter 주요 기능:
    // - 요청에서 사용자명/비밀번호 파라미터 추출
    // - UsernamePasswordAuthenticationToken 생성
    // - AuthenticationManager에 인증 위임
    // - 인증 성공/실패 핸들러 호출
    // - 성공 시 SecurityContext에 인증 정보 저장

    CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
    filter.setAuthenticationManager(authenticationManager);
    filter.setFilterProcessesUrl("/api/auth/login");

    filter.setAuthenticationSuccessHandler((request, response, authentication) -> {
      // 성공 처리 로직

      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

      User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
          () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")
      );

      // SecurityContext에 명시적으로 Authentication 저장
      SecurityContext context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(authentication);
      SecurityContextHolder.setContext(context);

      // 세션에 SecurityContext 저장
      HttpSession session = request.getSession(true);
      session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
          context);

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(new ObjectMapper().writeValueAsString(userMapper.toDto(user)));

    });
    filter.setAuthenticationFailureHandler((request, response, exception) -> {
      // 실패 처리 로직
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"status\":\"failure\"}");
    });
    return filter;
  }

  @Bean
  public SecurityFilterChain chain(HttpSecurity http) throws Exception {

    CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    tokenRepository.setCookieName("CSRF-TOKEN");    // 쿠키 이름 변경
    tokenRepository.setHeaderName("X-CSRF-TOKEN");  // 헤더 이름 변경

    //SSR 방식이었다면 아래 세션기반의 토큰 레포지토리를 사용하면되지만, 요구사항에서 우리는 CSR방식을 사용하기로 했으므로 위처럼
    //커스터마이징을 해주어야한다.
    //CsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();

    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/csrf-token", "/api/users", "/api/auth/login"
                , "/favicon.ico").permitAll()
            .requestMatchers("/api/auth/me", "/api/auth/logout").authenticated()
            .requestMatchers("/api/**").authenticated()
            .anyRequest().permitAll())

        // HTTP Basic 인증 비활성화
        .httpBasic(AbstractHttpConfigurer::disable)

        //디스코드잇은 CSR 방식이기 때문에 formLogin은 사용하지 않는다.
        .formLogin(AbstractHttpConfigurer::disable)

        .addFilter(authenticationFilter(
            authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))))

        // 로그아웃 필터 구현
        .addFilterBefore(new CustomLogoutFilter(), UsernamePasswordAuthenticationFilter.class)

        //securityContext 설정
        .securityContext(securityContext -> securityContext
            .securityContextRepository(securityContextRepository()))

        .csrf(csrf -> csrf
            .csrfTokenRepository(tokenRepository)
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
        )

        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .sessionFixation().changeSessionId()
            // 세션 고정 보호
            // none 로그인 시 세션 정보 변경을 안해서 해커에게 취약
            // new session 로그인 시 세션을 새로 생성 (기존 anonymous 세션 -> login 세션으로 바뀌면서 새로 만든다)
            // change Session 세션은 동일하나, 전달해주는 세션 cookie id 값을 다르게 반환하여 해커가 가진 값과 다르도록
            .maximumSessions(1) // 동시 로그인 가능 개수
            .maxSessionsPreventsLogin(false)// 동일 계정으로 로그인 했을때, 이미 로그인 되어있는 계정을 로그아웃 시킬지
        );

    return http.build();
  }

}


