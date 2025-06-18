package com.sprint.mission.discodeit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {

  // 특정 사용자의 모든 Remember-Me 토큰 삭제
    /*
    @DeleteMapping("/users/{userName}/rememberMe")
    public ResponseEntity<Map<String, Object>> revokeRememberMeTokens(@PathVariable String userName) {
        log.info("사용자 [{}]의 Remember-Me 토큰 관리자 삭제 요청", userName);

        HashMap<String, Object> response = new HashMap<>();
        response.put("userName", userName);
        response.put("status", "revoked");
        response.put("timestamp", Instant.now());

        log.info("사용자 [{}]의 모든 Remember-Me 토큰이 삭제 되었습니다", userName);

        return ResponseEntity.ok(response);
    }
    */

  // 전체 Remember-Me 토큰 통계 조회

    /*
    @GetMapping("/rememberMe/stats")
    public ResponseEntity<Map<String, Object>> getTokenStats() {
        Map<String, Object> stats = new HashMap<>();

        // 간단한 통계 수집

        Integer totalTokens = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.persistent_logins", Integer.class);

        Integer activeUsers = jdbcTemplate.queryForObject(
                "SELECT COUNT(DISTINCT userName) FROM public.persistent_logins", Integer.class);

        Integer recentTokens = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.persistent_logins WHERE last_used > (CURRENT_TIMESTAMP - INTERVAL '24 hours')",
                Integer.class);

        stats.put("totalTokens", totalTokens);
        stats.put("activeUsers", activeUsers);
        stats.put("recentlyUsedTokens", recentTokens);
        stats.put("timestamp", Instant.now());

        return ResponseEntity.ok(stats);
    }
     */

}
