package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

@RequiredArgsConstructor
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

  private final ObjectMapper objectMapper;

  @Override
  public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
      throws IOException, ServletException {
    int status = HttpServletResponse.SC_UNAUTHORIZED;
    HttpServletResponse response = event.getResponse();
    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    ErrorResponse errorResponse = new ErrorResponse(
        new SessionAuthenticationException("Session is expired."),
        status
    );
    errorResponse.getDetails().put("sessionId", event.getSessionInformation().getSessionId());
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
