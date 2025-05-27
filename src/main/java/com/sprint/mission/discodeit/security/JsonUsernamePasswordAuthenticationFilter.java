package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@RequiredArgsConstructor
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final ObjectMapper objectMapper; //안정적인 역직렬화를 위해 ObjectMapper를 주입

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    if(!request.getMethod().equals("POST")) {
      throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
    }
    try{
      //JSON 요청 본문 파싱
      LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),LoginRequest.class);
      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password());
      setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);
    }catch (IOException e){
      throw new AuthenticationServiceException("Request parsing failed", e);
    }
  }

  //필터를 초기화할 때 여러가지 설정을 하기 위한 메소드
  public static JsonUsernamePasswordAuthenticationFilter createDefault(
      ObjectMapper objectMapper,
      AuthenticationManager authenticationManager
  ) {
    JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter(objectMapper);
    filter.setRequiresAuthenticationRequestMatcher(SecurityMatchers.LOGIN);
    filter.setAuthenticationManager(authenticationManager);
    filter.setAuthenticationSuccessHandler(new CustomLoginSuccessHandler(objectMapper));
    filter.setAuthenticationFailureHandler(new CustomLoginFailureHandler(objectMapper));
    filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
    return filter;
  }
}
