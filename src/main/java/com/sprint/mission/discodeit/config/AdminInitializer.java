package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.role.Role;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) throws Exception {

    if (userRepository.existsByUsername("admin")) {
      return;
    }

    User admin = User.builder()
        .username("admin")
        .role(Role.ROLE_ADMIN)
        .email("admin@mail.com")
        .profile(null)
        .password(passwordEncoder.encode("qwerqwer1!"))
        .build();

    userRepository.save(admin);
  }
}
