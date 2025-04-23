package com.sprint.mission.discodeit; // 이 패키지 경로가 맞는지 확인하세요

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest // 통합 테스트 설정
@Testcontainers // Testcontainers 활성화
@ActiveProfiles("test") // application-test.yml 활성화
public abstract class IntegrationTestBase {

  @Container // JUnit 5에게 이 필드가 Testcontainer임을 알림
  static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine") // 사용할 PostgreSQL 이미지 지정 (버전 확인)
      .withDatabaseName("testdb") // 사용할 DB 이름 (선택 사항)
      .withUsername("testuser")   // 사용할 사용자 이름 (선택 사항)
      .withPassword("testpass");  // 사용할 비밀번호 (선택 사항)
  // .withInitScript("db/init.sql"); // 컨테이너 시작 시 실행할 추가 스크립트 경로 지정 (선택 사항, classpath 기준)

  // ApplicationContext가 로드되기 전에 동적으로 DataSource 관련 프로퍼티 설정
  @DynamicPropertySource
  static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgresContainer::getUsername);
    registry.add("spring.datasource.password", postgresContainer::getPassword);
    // PostgreSQL 드라이버 클래스 이름은 보통 자동 감지되므로 명시적 설정은 생략 가능
    // registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
  }
}