package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.RoleRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  @Override
  public void run(String... args) throws Exception {
    initializeAdminAccount();
  }

  private void initializeAdminAccount() {

    Role adminRole = createRoleIfNotExists("ROLE_ADMIN");
    Role managerRole = createRoleIfNotExists("ROLE_CHANNEL_MANAGER");
    Role userRole = createRoleIfNotExists("ROLE_USER");

    if (!userRepository.existsByUsername("admin")) {
      User admin = User.builder()
          .username("admin")
          .password(passwordEncoder.encode("adminPassword00"))
          .email("admin@example.com")
          .roles(Set.of(adminRole))
          .build();
      userRepository.save(admin);
      log.debug("admin 계정 생성 완료");
    }
  }

  private Role createRoleIfNotExists(String roleName) {
    return roleRepository.findByName(roleName)
        .orElseGet(() -> {
          Role role = new Role(roleName);
          return roleRepository.save(role);
        });
  }
}
