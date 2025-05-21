package com.sprint.mission.discodeit.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.response.ErrorResponse;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.info("로그인 성공");

    // Authentication 객체에서 UserDetails 가져오기
    Object principal = authentication.getPrincipal();
    String username;

    if (principal instanceof UserDetails) {
      username = ((UserDetails) principal).getUsername();
    } else {
      log.warn("Authentication 의 Principal 이 UserDetails 타입이 아닙니다. : {}", principal.getClass());
      username = principal.toString();
      // response
      sendErrorResponse(
          response,
          HttpStatus.INTERNAL_SERVER_ERROR,
          "INVALID_AUTH_PRINCIPAL_TYPE",
          "로그인 후 사용자 정보를 처리하는 중에 Principal이 UserDetails가 아니기에 발생한 오류",
          Map.of("principalClassType", principal.getClass().getTypeName()),
          "InvalidPrincipalTypeException"
      );
    }

    try {
      // User 조회
      User user = userRepository.findByUsername(username)
          .orElseThrow(() -> new UserNotFoundException(Map.of("username", username)));
      UserDto userDto = userMapper.toDto(user);

      response.setStatus(HttpStatus.OK.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");

      objectMapper.writeValue(response.getWriter(), userDto);

    } catch (
        UserNotFoundException e) { // 전역 예외처리는 Spring MVC Controller 계층에서 이뤄져서 직접 response를 작성해야 한다
      log.error("로그인 성공 후 사용자 정보 조회 실패: {}", e.getMessage());
      // response
      sendErrorResponse(
          response,
          ErrorCode.USER_NOT_FOUND.getStatus(),
          ErrorCode.USER_NOT_FOUND.name(),
          e.getMessage(),
          e.getDetails(),
          e.getClass().getSimpleName());
    } catch (Exception e) {
      log.error("로그인 성공 처리 중 오류 발생: {}", e.getMessage());
      // response
      sendErrorResponse(
          response,
          HttpStatus.INTERNAL_SERVER_ERROR,
          "INTERNAL_SERVER_ERROR",
          e.getMessage(),
          Map.of("username", username),
          e.getClass().getSimpleName()
      );
    }
  }

  private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String code,
      String message, Map<String, Object> details, String exceptionType) throws IOException {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    ErrorResponse errorResponse = new ErrorResponse(
        Instant.now(),
        code,
        message,
        details,
        exceptionType,
        status.value()
    );

    objectMapper.writeValue(response.getWriter(), errorResponse);
  }
}
