package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) {
    String adminEmail = "admin@example.com";
    if (!userRepository.existsByEmail(adminEmail)) {
      User admin = new User("admin", adminEmail, passwordEncoder.encode("qwer1234!"), null, Role.ROLE_ADMIN);
      UserStatus userStatus = new UserStatus(admin, Instant.now());

      userRepository.save(admin);
    }
  }
}

