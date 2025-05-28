package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.security.sasl.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
// json 형식의 로그인 요청 본문 파싱
public class JsonUsernamePasswordAuthenticationFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;
  private final RememberMeServices rememberMeServices;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final SessionAuthenticationStrategy sessionAuthenticationStrategy;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // 로그인 엔드포인트 및 POST 방식이 아니라면 다음 필터로 넘기기
    if (!request.getRequestURI().equals("/api/auth/login") || !request.getMethod()
        .equalsIgnoreCase("POST")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      // 요청 본문에서 JSON 데이터를 LoginRequest 객체로 역직렬화
      LoginRequest login = objectMapper.readValue(request.getInputStream(),
          LoginRequest.class);

      // 인증 요청 토큰 생성 (credentials는 password 포함)
      UsernamePasswordAuthenticationToken authRequestToken = new UsernamePasswordAuthenticationToken(
          login.username(), login.password());

      // 인증 매니저에게 인증 요청 위임
      Authentication authenticate = authenticationManager.authenticate(authRequestToken);
      log.info("principal class = {}", authenticate.getPrincipal().getClass());

      // 인증 성공 시 SecurityContext에 저장
      SecurityContext context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(authenticate);

      request.getSession(true);
      // 세션 기반 보안 컨텍스트 저장소에 context 저장
      new HttpSessionSecurityContextRepository().saveContext(context, request, response);

      sessionAuthenticationStrategy.onAuthentication(authenticate, request, response);

      // SessionRegistry 등록 직후 principal 개수
      sessionAuthenticationStrategy.onAuthentication(authenticate, request, response);

      // Remember Me 처리 (파라미터가 있는 경우)
      rememberMeServices.loginSuccess(request, response, authenticate);
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write("{\"message\":\"login success\"}");

    } catch (AuthenticationException ae) {
      // 인증 실패 시 401 Unauthorized
      rememberMeServices.loginFail(request, response); // Remember Me 실패 처리
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write("{\"message\":\"Invalid credentials\"}");
    }
  }
}
