package com.sprint.mission.discodeit.security.cookie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersistentTokenCleanupTask {

  private final JdbcTemplate jdbcTemplate;

  // 오래된 토큰 정리 작업
  @Scheduled(cron = "0 0 3 * * *")
  public void purgeExpiredToken() {
    log.info("오래된 Remember-me 토큰 정리 작업 시작");

    int deletedCount = jdbcTemplate.update("""
        DELETE FROM persistent_logins
        WHERE last_used < (CURRENT_TIMESTAMP - INTERVAL '90 days')
        """);

    log.info("오래된 Remember-me 토큰 정리 작업 완료: {}개 제거", deletedCount);

    // 매트릭 업데이트
    updateTokenMetrics();
  }

  // 토큰 매트릭 수집
  private void updateTokenMetrics() {
    // 활성 토큰 수 조회
    Integer totalTokens = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM persistent_logins",
        Integer.class
    );
    // 90일 이상 사용되지 않은 토큰 수
    Integer oldTokens = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM persistent_logins WHERE last_used < (CURRENT_TIMESTAMP - INTERVAL '90 days')",
        Integer.class
    );

    log.info("현재 활성 토근: {}, 오래된 토큰: {}", totalTokens, oldTokens);
  }

}
