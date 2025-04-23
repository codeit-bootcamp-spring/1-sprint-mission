package com.sprint.mission.discodeit.config; // 패키지 경로는 실제 프로젝트 구조에 맞게 조정하세요.

import com.sprint.mission.discodeit.storage.s3.S3BinaryContentStorage;
import com.sprint.mission.discodeit.storage.s3.S3StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties; // S3StorageProperties 빈 생성을 위해 필요합니다.
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// S3StorageProperties 클래스가 application.yaml 등의 프로퍼티와 바인딩되어
// 빈으로 생성될 수 있도록 활성화합니다.
@EnableConfigurationProperties(S3StorageProperties.class)
// application.yaml 또는 환경변수에서 discodeit.storage.type=s3 일 때만
// 아래 설정들이 로드되도록 조건을 설정합니다.
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3", matchIfMissing = false)
public class S3StorageConfig {

  /**
   * S3BinaryContentStorage 빈을 생성하여 스프링 컨테이너에 등록합니다.
   * 파라미터로 S3StorageProperties 빈을 주입받습니다. (스프링이 자동으로 처리)
   * @param s3StorageProperties 스프링이 생성하고 설정값을 채워 주입해주는 빈
   * @return 설정값이 반영된 S3BinaryContentStorage 인스턴스
   */
  @Bean // 이 메소드가 빈(Bean)을 생성함을 스프링에게 알립니다.
  public S3BinaryContentStorage s3BinaryContentStorage(S3StorageProperties s3StorageProperties) {
    // S3BinaryContentStorage 생성자에 String, int 등을 직접 넣는 대신,
    // 스프링이 설정값을 담아 주입해준 S3StorageProperties 객체를 통째로 전달합니다.
    return new S3BinaryContentStorage(s3StorageProperties);
  }
}