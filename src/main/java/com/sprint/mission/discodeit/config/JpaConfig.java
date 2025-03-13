package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing  // 엔티티 자동 감시 활성화 - 엔티티 생성/수정 시 자동으로 날짜를 기록하는 기능 사용 시 필수
public class JpaConfig {
}
