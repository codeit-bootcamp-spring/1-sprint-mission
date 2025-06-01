package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpSessionEvent;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  @Value("${discodeit.admin.username}")
  private String username;
  @Value("${discodeit.admin.password}")
  private String password;
  @Value("${discodeit.admin.email}")
  private String email;
  private final PasswordEncoder passwordEncoder;
  private final SessionRegistry sessionRegistry;

  private static final Map<String, String> sessionUserMap = new ConcurrentHashMap<>();

  public UserDto getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
      String username = userDetails.getUsername();
      User user = userRepository.findByUsername(username)
          .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
      return userMapper.toDto(user);
    } else {
      throw new UsernameNotFoundException("현재 로그인한 사용자를 찾을 수 없습니다.");
    }
  }

  @Transactional
  @Override
  public UserDto initAdmin() {
    if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
      log.warn("이미 어드민이 존재합니다.");
      return null;
    }

    String encodedPassword = passwordEncoder.encode(password);
    User admin = new User(username, email, encodedPassword, null);
    admin.updateRole(Role.ADMIN);
    userRepository.save(admin);

    UserDto adminDto = userMapper.toDto(admin);
    log.info("어드민이 초기화되었습니다. {}", adminDto);
    return adminDto;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  @Override
  public UserDto updateRole(RoleUpdateRequest request) {
    UUID userId = request.userId();
    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));
    user.updateRole(request.newRole());

    sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> ((DiscodeitUserDetails) principal).getUserDto().id().equals(userId))
        .findFirst()
        .ifPresent(principal -> {
              List<SessionInformation> activeSessions = sessionRegistry.getAllSessions(principal,
                  false);
              log.debug("Active sessions: {}", activeSessions.size());
              activeSessions.forEach(SessionInformation::expireNow);
            }
        );

    return userMapper.toDto(user);
  }
}
