package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  public CustomAuthenticationFilter(AuthenticationManager authManager) {
    super.setAuthenticationManager(authManager);
    setFilterProcessesUrl("/api/auth/login"); // 로그인 경로 설정
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {

    try {
      LoginRequest loginRequest = new ObjectMapper()
          .readValue(request.getInputStream(), LoginRequest.class);

      UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
          loginRequest.username(),
          loginRequest.password()
      );

      return this.getAuthenticationManager().authenticate(authRequest);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
