package com.sprint.mission.discodeit.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,

      HttpServletResponse response) throws AuthenticationException {

    // Content-Type이 JSON인 경우
    if (request.getContentType() != null &&
        request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
      try {
        // JSON에서 username과 password 추출시도
        LoginRequest loginRequest = objectMapper.readValue(
            request.getInputStream(), LoginRequest.class);

        String username = loginRequest.username();
        String password = loginRequest.password();

        if (username == null) {
          username = "";
        }
        if (password == null) {
          password = "";
        }

        UsernamePasswordAuthenticationToken authRequest =
            new UsernamePasswordAuthenticationToken(username, password);
        //UsernamePasswordAuthenticationToken에 명시적으로 넣어주기(form 형태가 아닌 Json으로 받으므로!)

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
      } catch (IOException e) {
        throw new AuthenticationServiceException("JSON 요청 파싱 예외", e);
      }
    }

    // 기본 form 데이터 처리 (fallback)
    return super.attemptAuthentication(request, response);
  }

  @Override
  protected void setDetails(HttpServletRequest request,
      UsernamePasswordAuthenticationToken authRequest) {
    authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
  }
}
