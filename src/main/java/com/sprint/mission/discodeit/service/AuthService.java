package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.User.Role;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final SessionRegistry sessionRegistry;
  private final PasswordEncoder passwordEncoder;

  @Value("${discodeit.admin.username}")
  private String adminUsername;
  @Value("${discodeit.admin.email}")
  private String adminEmail;
  @Value("${discodeit.admin.password}")
  private String adminPassword;

  @PostConstruct
  @Transactional
  public UserDto initAdmin() {
    if (userRepository.existsByEmail(adminEmail) || userRepository.existsByUsername(adminUsername)) {
      log.warn("이미 어드민이 존재합니다.");
      return null;
    }

    String encodedPassword = passwordEncoder.encode(adminPassword);
    User admin = User.create(adminUsername, adminEmail, encodedPassword);
    admin.updateRole(Role.ADMIN);
    userRepository.save(admin);

    UserDto adminDto = userMapper.toDto(admin);
    log.info("어드민이 초기화되었습니다. {}", adminDto);
    return adminDto;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  public UserDto updateUserRole(RoleUpdateRequest roleUpdateRequest) {
    UUID userId = roleUpdateRequest.userId();
    Role newRole = roleUpdateRequest.newRole();
    User user = userRepository.findById(roleUpdateRequest.userId())
        .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

    if (newRole != null) {
      user.updateRole(newRole);
    }

    sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> ((DiscodeitUserDetails) principal).getUsername().equals(user.getUsername()))
        .findFirst()
        .ifPresent(principal -> {
              sessionRegistry.getAllSessions(principal, false)
                  .forEach(SessionInformation::expireNow);
            }
        );

    return userMapper.toDto(user);
  }

}
