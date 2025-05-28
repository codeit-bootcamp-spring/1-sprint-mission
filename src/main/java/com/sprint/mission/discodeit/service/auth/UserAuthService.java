package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.role.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.jwt.JwtTokenNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.status.UserSessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthService implements com.sprint.mission.discodeit.service.auth.LoginRequest {

  private final JwtService jwtService;
  private final JwtSessionRepository jwtSessionRepository;
  private final UserRepository userRepository;
  private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
  private final UserSessionService userSessionService;


  /**
   * @methodName : updateRole
   * @date : 2025. 5. 19. 15:42
   * @author : wongil
   * @Description: 사용자 역할 수정
   **/
  @Override
  @Transactional
  public void updateRole(RoleUpdateRequest request, HttpServletRequest httpRequest) {

    User user = userRepository.findById(request.userId()).orElseThrow(
        () -> new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
            Map.of(ErrorCode.USER_NOT_FOUND.getCode(), ErrorCode.USER_NOT_FOUND.getMessage())));

    user.changeRole(request.newRole());

    jwtService.invalidTokenByUserId(request.userId());
    log.warn("유저 [{}] JWT Token 삭제", request.userId());
  }

  /**
   * @methodName : sessionMe
   * @date : 2025. 5. 27. 18:44
   * @author : wongil
   * @Description: 세션 방식 유지
   **/
  public UserDto sessionMe(HttpSession session) {

    SecurityContext context = (SecurityContext) session.getAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
    if (context == null || context.getAuthentication() == null || !context.getAuthentication()
        .isAuthenticated()) {
      return null;
    }

    UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
    User user = userRepository.findUserByUsername(principal.getUsername());
    BinaryContent profile = user.getProfile();

    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .profile(new BinaryContentDto(profile.getId(), profile.getFileName(), profile.getSize(),
            profile.getContentType()))
        .Role(user.getRole())
        .online(userSessionService.isOnline(user.getUsername()))
        .build();
  }

  public ResponseEntity<String> tokenMe(String refreshToken) {
    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new JwtTokenNotFoundException(Instant.now(), ErrorCode.NOT_FOUND_JWT,
            Map.of(
                ErrorCode.NOT_FOUND_JWT.getCode(),
                ErrorCode.NOT_FOUND_JWT.getMessage()
            )));

    log.info("me Jwt Access Token 출력: {}", jwtSession.getRefreshToken());
    return ResponseEntity.ok(jwtSession.getAccessToken());
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      Arrays.stream(cookies)
          .forEach(cookie -> {
            if (cookie.getName().equals("refresh_token")) {
              String value = cookie.getValue();

              jwtService.invalidRefreshToken(value);

              Cookie kookie = new Cookie("refresh_token", null);
              kookie.setMaxAge(0);
              kookie.setHttpOnly(false);
              kookie.setPath("/");
              response.addCookie(kookie);
            }
          });
    }
  }
}
