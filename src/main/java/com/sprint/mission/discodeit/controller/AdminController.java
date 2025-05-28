package com.sprint.mission.discodeit.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

  private final PersistentTokenRepository tokenRepository;
  private final JdbcTemplate jdbcTemplate;

  // todo: 서비스로 로직을 빼는게 나을 것 같긴함;

  // 특정 사용자의 모든 Remember-Me 토큰 삭제
  @DeleteMapping("/users/{username}/rememberMe")
  public ResponseEntity<Map<String, Object>> revokeRememberMeTokens(@PathVariable String username) {
    log.info("사용자 [{}]의 Remember-Me 토큰 관리자 삭제 요청", username);
    tokenRepository.removeUserTokens(username);

    HashMap<String, Object> response = new HashMap<>();
    response.put("username", username);
    response.put("status", "revoked");
    response.put("timestamp", Instant.now());

    log.info("사용자 [{}]의 모든 Remember-Me 토큰이 삭제 되었습니다", username);

    return ResponseEntity.ok(response);
  }

  // 전체 Remember-Me 토큰 통계 조회
  @GetMapping("/rememberMe/stats")
  public ResponseEntity<Map<String, Object>> getTokenStats() {
    Map<String, Object> stats = new HashMap<>();

    // 간단한 통계 수집

    Integer totalTokens = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM public.persistent_logins", Integer.class);

    Integer activeUsers = jdbcTemplate.queryForObject(
        "SELECT COUNT(DISTINCT username) FROM public.persistent_logins", Integer.class);

    Integer recentTokens = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM public.persistent_logins WHERE last_used > (CURRENT_TIMESTAMP - INTERVAL '24 hours')",
        Integer.class);

    stats.put("totalTokens", totalTokens);
    stats.put("activeUsers", activeUsers);
    stats.put("recentlyUsedTokens", recentTokens);
    stats.put("timestamp", Instant.now());

    return ResponseEntity.ok(stats);
  }

}
