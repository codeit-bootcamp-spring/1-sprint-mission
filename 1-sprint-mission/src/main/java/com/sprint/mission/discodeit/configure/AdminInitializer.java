package com.sprint.mission.discodeit.configure;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  @PostConstruct
  public void init() {
    if (!userRepository.existsByUsername("admin")) {
      User admin = new User("admin", "admin@example.com", passwordEncoder.encode("admin123"), null);
      admin.setRoles(Set.of(Role.ROLE_ADMIN));
      userRepository.save(admin);
    }
  }
}
