package com.sprint.mission.discodeit.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.entity.Role;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
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
  private final SessionRegistry sessionRegistry;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.info("로그인 성공");

    // Authentication 객체에서 UserDetails 가져오기
    Object principal = authentication.getPrincipal();
    String username;

    CustomUserDetails cudPrincipal = null; // SessionRegistry 조회용 CustomUserDetails

    if (principal instanceof CustomUserDetails) {
      username = ((CustomUserDetails) principal).getUsername();
      cudPrincipal = (CustomUserDetails) principal;

      String rolesString = userRepository.findByUsername(username)
          .map(User::getRoles)
          .map(roles -> roles.stream()
              .map(Role::getName)
              .collect(Collectors.joining(", "))) // List<String> -> "ROLE_USER, ROLE_ADMIN"
          .orElse("역할 없음"); // 사용자를 못 찾거나 역할이 없으면

      log.info("사용자 '{}'의 역할 확인: roles={}", username, rolesString);

      // 세션 관련 오류로 디버깅 -----------------------------------------------------------------------
      log.info("사용자 '{}' 로그인 성공 (Principal is CustomUserDetails): ",
          cudPrincipal.getUsername());
      log.info("Principal 객체 정보: class={}, username={}, hashCode={}",
          cudPrincipal.getClass().getName(),
          cudPrincipal.getUsername(),
          cudPrincipal.hashCode()
      );

      if (sessionRegistry != null) {
        List<SessionInformation> sessions = sessionRegistry.getAllSessions(
            cudPrincipal, false);
        if (sessions != null && !sessions.isEmpty()) {
          log.info("!!! LoginSuccessHandler: SessionRegistry에서 사용자 '{}'의 활성 세션 {}개 찾음 !!!",
              cudPrincipal.getUsername(), sessions.size());
        } else {
          log.warn(
              "!!! INFO: LoginSuccessHandler에서 현재 Principal ('{}')로 세션 못찾음. 이제 Registry 전체를 확인합니다. !!!",
              cudPrincipal.getUsername());
          debugMissingSessionRegistry(cudPrincipal);
        }
      } else {
        log.warn("sessionRegistry 없음");
      }
      // 세션 관련 오류로 디버깅 ----------------------------------------------------------------------
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

  private void debugMissingSessionRegistry(CustomUserDetails userDetails) {
    if (userDetails == null) {
      log.warn("debugMissingSessionRegistry : userDetails 가 null 이라서 비교 수행 x");
    }
    if (sessionRegistry == null) {
      log.warn("debugMissingSessionRegistry : SessionRegistry is null. (주입 문제) ");
      return;
    }
    List<Object> allPrincipalsInRegistry = sessionRegistry.getAllPrincipals();
    log.info("!!! SessionRegistry에 등록된 전체 Principal 정보 (총 {}개) !!!",
        allPrincipalsInRegistry.size());
    if (allPrincipalsInRegistry.isEmpty()) {
      log.info("SessionRegistry에 등록된 Principal이 없습니다. (SecurityConfig ... maximum 설정이 잘못됐나?)");
      return;
    }
    boolean foundMatchInRegistry = false;
    for (Object p : allPrincipalsInRegistry) {
      log.info("Principals : class={}, toString={}", p.getClass().getName(), p.toString());
      if (p instanceof CustomUserDetails) {
        CustomUserDetails registeredCud = (CustomUserDetails) p;
        boolean isEqual = (userDetails != null) && registeredCud.equals(userDetails);
        log.info("(CustomUserDetails) username={}, hashCode={}, 넘어온 targetUserDetails 와 equals={}",
            registeredCud.getUsername(),
            registeredCud.hashCode(),
            isEqual);

        if (isEqual) {
          foundMatchInRegistry = true;
          List<SessionInformation> why = sessionRegistry.getAllSessions(registeredCud, false);
          log.info("      ---> 이 등록된 Principal로 조회한 세션 수: {}",
              (why != null
                  ? why.size() : "null or 0"));
        }
      } else if (p instanceof UserDetails) {
        UserDetails otherUserDetails = (UserDetails) p;
        log.info("(Other UserDetails) username={}, hashCode={}",
            otherUserDetails.getUsername(),
            otherUserDetails.hashCode());
      }
    }
    if (userDetails == null) {
      log.info("UserDetails 가 null 이므로 SessionRegistry 내 일치 여부 비교는 건너뜀");
    }
    log.info(
        "------------------------------SessionRegistry 정보 로깅 끝-----------------------------------");
  }
}
