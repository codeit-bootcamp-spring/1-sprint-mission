package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.security.JwtSessionNotFoundException;
import com.sprint.mission.discodeit.exception.security.UserUnauthorizedException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.RoleRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {


  private final UserRepository userRepository;
  //
  private final UserDetailsService userDetailsService;
  //
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;
  private final SessionRegistry sessionRegistry;
  private final JwtSessionRepository jwtSessionRepository;

  public UserDto getUserBySession() {
    log.info("세션 활용 사용자 정보 조회 시작");

    // SecurityContextHolder 로 Authentication 객체 들고오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // authentication 유효성 검증
    if (authentication == null || !authentication.isAuthenticated()
        || !(authentication.getPrincipal() instanceof UserDetails)) {
      throw new UserUnauthorizedException(Map.of(
          "isNull", authentication == null,
          "isAuthenticated", authentication != null && authentication.isAuthenticated(),
          "principal", authentication != null ? authentication.getPrincipal().toString() : "null"
      ));
    }

    Object principal = authentication.getPrincipal();
    String username = ((UserDetails) principal).getUsername();
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(Map.of("username", username)));

    log.info("세션 활용 사용자 정보 조회 성공");
    return userMapper.toDto(user);
  }

  @Transactional
  public UserDto changeRole(RoleUpdateRequest request) {
    log.info("사용자 권한 변경");
    log.debug("사용자 ID {}의 권한 {}로 변경합니다.", request.userId(), request.newRole());

    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new UserNotFoundException(Map.of("userId", request.userId())));

    Role role = roleRepository.findByName(request.newRole().getName())
        .orElseThrow(() ->
            new NoSuchElementException("Role이 존재하지 않습니다 : " + request.newRole().getName())
        );
    Set<Role> newRoles = Set.of(role);

    user.updateRole(newRoles);
    User updatedUser = userRepository.saveAndFlush(user);

    // JwtSession 로 강제 로그아웃 처리 ---------------------------------------------------------------
    UUID userId = updatedUser.getId();
    List<JwtSession> userSessions = jwtSessionRepository.findAllbyUserIdAndRevokedFalse(userId);

    if (userSessions.isEmpty()) {
      log.info("사용자 ID {}의 활성 JwtSession 이 없습니다.", userId);
    } else {
      log.info("사용자 ID {} 의 활성 JwtSession {}개 무효화합니다.", userId, userSessions.size());
      for (JwtSession session : userSessions) {
        session.updatedRevoked(true);
        jwtSessionRepository.save(session);
      }
    }

    // 세션 무효화 (로그아웃)
//    String userName = updatedUser.getUsername();
//    if (userName != null && !userName.isEmpty()) {
//      // 만료된 세션 제외
//      UserDetails userDetailsPrincipal =
//          userDetailsService.loadUserByUsername(userName);
//
//      List<SessionInformation> sessions = sessionRegistry.getAllSessions(
//          userDetailsPrincipal,
//          false);
//
//      if (sessions != null && !sessions.isEmpty()) {
//        log.debug("사용자 {} 활성 세션 {} 개 무효화", userName, sessions.size());
//        for (SessionInformation sessionInformation : sessions) {
//          sessionInformation.expireNow();
//          log.debug("세션 ID {} 만료", sessionInformation.getSessionId());
//        }
//      } else {
//        log.info("사용자 {} 의 활성 세션이 없습니다.", userName);
//      }
//    } else {
//      log.warn("사용자 ID {}의 principal 정보가 없어 세션을 무효화시킬 수 없습니다.", userName);
//    }
//
//    log.info("사용자 권한 변경 완료");
    return userMapper.toDto(updatedUser);
  }
}
