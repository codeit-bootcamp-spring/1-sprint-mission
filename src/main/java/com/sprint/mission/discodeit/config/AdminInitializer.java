package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.Role;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.admin.username}")
  private String adminUsername;

  @Value("${app.admin.password}")
  private String adminPassword;

  @Value("${app.admin.email}")
  private String adminEmail;

  @Bean
  public ApplicationRunner initAdmin() {
    return args -> {
      if(userRepository.findByUsername(adminUsername).isEmpty()) {
        String encodedPassword = passwordEncoder.encode(adminPassword);
        User admin = new User(adminUsername, adminEmail, encodedPassword, null, Role.ROLE_ADMIN);
        userRepository.save(admin);
      }
    };
  }
}
