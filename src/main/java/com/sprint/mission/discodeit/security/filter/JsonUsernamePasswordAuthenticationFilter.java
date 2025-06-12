package com.sprint.mission.discodeit.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.security.CustomLoginFailureHandler;
import com.sprint.mission.discodeit.security.CustomLoginSuccessHandler;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@RequiredArgsConstructor
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper;
  private final JwtService jwtService;

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

  public static class Configurer extends
      AbstractAuthenticationFilterConfigurer<HttpSecurity, Configurer, JsonUsernamePasswordAuthenticationFilter> {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    public Configurer(ObjectMapper objectMapper, JwtService jwtService) {
      super(new JsonUsernamePasswordAuthenticationFilter(objectMapper, jwtService),
          SecurityMatchers.LOGIN_URL);
      this.objectMapper = objectMapper;
      this.jwtService = jwtService;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
      return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.POST.name());
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
      loginProcessingUrl(SecurityMatchers.LOGIN_URL);
      successHandler(new CustomLoginSuccessHandler(objectMapper, jwtService));
      failureHandler(new CustomLoginFailureHandler(objectMapper));
    }
  }
}
