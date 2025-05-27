package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  @Override
  public void run(ApplicationArguments args) throws Exception {
    initAdminAccount();
  }

  private void initAdminAccount() {
    log.info("관리자 계정 생성 요청");
    String adminUsername = "admin";
    String adminEmail = "admin@codeit.com";
    String adminPassword = "admin1234";

    if (userRepository.existsByEmail(adminEmail)) {
      log.info("관리자 계정이 이미 존재합니다: E-mail = {}", adminEmail);
      return;
    }
    User admin = new User(adminUsername, adminEmail, passwordEncoder.encode(adminPassword), null);
    admin.updateRole(Role.ADMIN);
    log.info("관리자 계정 생성 완료: E-mail = {}", adminEmail);
    userRepository.save(admin);
  }

}
