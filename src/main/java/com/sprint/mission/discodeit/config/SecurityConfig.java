package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.CustomPermissionEvaluator;
import com.sprint.mission.discodeit.dto.CustomUserDetails;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.filter.CustomAuthenticationFilter;
import com.sprint.mission.discodeit.filter.CustomLogoutFilter;
import com.sprint.mission.discodeit.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserRepository userRepository;
  private final DataSource dataSource;

  //권한 변경 이벤트를 위한 세션 레지스트리 빈 등록
  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource);

    // 최초 구동 시 true로 설정하면 테이블 자동 생성
    // 테이블 생성 후 false로 변경하는게 안전
    tokenRepository.setCreateTableOnStartup(false);

    return tokenRepository;
  }

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
  public PersistentTokenBasedRememberMeServices rememberMeServices() {
    PersistentTokenBasedRememberMeServices rememberMeServices =
        new PersistentTokenBasedRememberMeServices("discodeitSecretKey123",
            userDetailsService(), persistentTokenRepository());

    rememberMeServices.setParameter("remember-me");
    rememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 21);

    return rememberMeServices;
  }


  @Bean
  public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
    return new RememberMeAuthenticationProvider("discodeitSecretKey123");
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
  public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    List<SessionAuthenticationStrategy> strategies = new ArrayList<>();
    //인증된 사용자의 세션을 등록하고 추적
    strategies.add(new RegisterSessionAuthenticationStrategy(sessionRegistry()));
    //세션 고정 공격(Session Fixation Attack) 방어 -> 세션 고정 보호는 SecurityFilterChain에서 처리하므로 제거
    //strategies.add(new SessionFixationProtectionStrategy());

    //여러 세션 전략을 조합하여 순차적으로 실행
    return new CompositeSessionAuthenticationStrategy(strategies);

    // 인증 성공 시 실행 순서:
    //1. RegisterSessionAuthenticationStrategy 실행
    //   → sessionRegistry.registerNewSession() 호출
    //   → 사용자별 세션 목록에 현재 세션 등록
    //
    //2. SessionFixationProtectionStrategy 실행
    //   → 현재 세션 ID 무효화
    //   → 새로운 세션 ID 생성 및 할당
    //   → 세션 데이터는 새 세션으로 이전
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.fromHierarchy("""
        ROLE_ADMIN > ROLE_CHANNEL_MANAGER > ROLE_USER
        """);
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
    // - 요청에서 사용자명/비밀번호 파라미터 추출 --> 하지만 우리는 form로그인이 아니기 때문에 직접 Json에서 추출해야함
    // - UsernamePasswordAuthenticationToken 생성
    // - AuthenticationManager에 인증 위임
    // - 인증 성공/실패 핸들러 호출
    // - 성공 시 SecurityContext에 인증 정보 저장

    CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
    filter.setAuthenticationManager(authenticationManager);
    filter.setFilterProcessesUrl("/api/auth/login");

    // SessionAuthenticationStrategy 설정 추가
    filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());

    // Remember-Me 서비스 설정
    filter.setRememberMeServices(rememberMeServices());

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

      // Remember-Me 토큰 생성 및 저장
      rememberMeServices().loginSuccess(request, response, authentication);
      log.info("Remember-Me 토큰 생성 시도");

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      //순환참조 피하기위해 UserDto 구조에 맞게 Map으로 응답 데이터 구성
      Map<String, Object> responseData = new HashMap<>();
      responseData.put("id", user.getId());
      responseData.put("username", user.getUsername());
      responseData.put("email", user.getEmail());
      responseData.put("online", true); // 로그인 직후이므로 true
      responseData.put("profile", user.getProfile() != null ?
          Map.of("id", user.getProfile().getId()) : null); // BinaryContentDto 간단히 처리
      responseData.put("role", user.getRole());

      response.getWriter()
          .write(new ObjectMapper().writeValueAsString(responseData));
    });
    filter.setAuthenticationFailureHandler((request, response, exception) -> {
      // Remember-Me 실패 처리
      rememberMeServices().loginFail(request, response);

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("{\"status\":\"failure\"}");
    });
    return filter;
  }

  @Bean
  public SecurityFilterChain chain(HttpSecurity http)
      throws Exception {

    CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    PersistentTokenRepository rememberMeTokenRepository = persistentTokenRepository();

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

            // 퍼블릭 채널 생성
            .requestMatchers(HttpMethod.POST, "/api/channels/public")
            .hasAnyRole("CHANNEL_MANAGER", "ADMIN")
            // 퍼블릭 채널 수정
            .requestMatchers(HttpMethod.PATCH, "/api/channels/public")
            .hasAnyRole("CHANNEL_MANAGER", "ADMIN")
            // 퍼블릭 채널 삭제
            .requestMatchers(HttpMethod.DELETE, "/api/channels/public")
            .hasAnyRole("CHANNEL_MANAGER", "ADMIN")

            // 사용자 권한 수정
            .requestMatchers(HttpMethod.PATCH, "/api/auth/role").hasRole("ADMIN")

            .requestMatchers("/api/**").hasRole("USER")
            .anyRequest().permitAll())

        // HTTP Basic 인증 비활성화
        .httpBasic(AbstractHttpConfigurer::disable)

        //디스코드잇은 CSR 방식이기 때문에 formLogin은 사용하지 않는다.
        .formLogin(AbstractHttpConfigurer::disable)

        .addFilter(authenticationFilter(
            authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))))

        // 로그아웃 필터 구현
        .addFilterBefore(new CustomLogoutFilter(sessionRegistry(), persistentTokenRepository()),
            UsernamePasswordAuthenticationFilter.class)

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
            .sessionRegistry(sessionRegistry())// 세션 이벤트 처리를 위해 필요
        )
        .rememberMe(rememberMe -> rememberMe
            .rememberMeServices(rememberMeServices())
        );

    return http.build();
  }

}


