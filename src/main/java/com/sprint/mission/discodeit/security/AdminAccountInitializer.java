package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    String adminPassword = System.getenv("ADMIN_PASSWORD");
    if (adminPassword == null) {
      log.warn("No admin password configured");
      adminPassword = "admin123"; // 기본값
    }

    if (userRepository.findByUsername("admin").isEmpty()) {
      User adminUser = new User();
      adminUser.setUsername("admin");
      adminUser.setEmail("admin@discodeit.com");
      adminUser.setPassword(passwordEncoder.encode(adminPassword));
      adminUser.setRole(Role.ADMIN);

      userRepository.save(adminUser);

      log.info("Admin account created");
    }
  }

  // CommandLineRunner
  // Spring Boot 애플리케이션이 시작될 때 특정 코드를 실행할 수 있게 해주는 인터페이스
  // - 애플리케이션 초기화 용도: 데이터베이스 초기 데이터 로드, 시스템 설정 등에 활용
  // - 단일 메서드: run(String... args) 단 하나의 메서드만 구현하면 됨
  // - 실행 시점: 모든 빈이 생성되고 애플리케이션 컨텍스트가 완전히 로딩된 후 실행
  // - 커맨드 라인 인자 접근: 애플리케이션 실행 시 전달된 인자에 접근 가능
}
