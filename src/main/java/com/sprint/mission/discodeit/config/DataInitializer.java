package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(ApplicationArguments args) throws Exception {
    String adminUsername = "admin";
    String adminEmail = "admin@codeit.kr";
    String adminPassword = "admin";

    boolean adminExists = userRepository.existsByEmailOrUsername(adminEmail, adminUsername);
    if (adminExists) return;

    User admin = User.createAdmin(adminUsername, adminEmail, passwordEncoder.encode(adminPassword));

    userRepository.save(admin);
  }



}
