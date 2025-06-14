package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.service.basic.NotificationService;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  /**
   * 알림 조회, 확인(삭제)은 요청자 본인의 알림에 대해서만 수행할 수 있습니다.
   * <p>
   * 알림 조회 및 삭제 시 현재 요청의 인증 정보로 요청자를 식별합니다.
   **/
  @GetMapping
  public ResponseEntity<List<NotificationDto>> getNotifications(
      @AuthenticationPrincipal UserPrincipal userPrincipal
  ) {
    String username = userPrincipal.getName();

    log.info("알림 조회 시도");
    List<NotificationDto> notificationDtos = notificationService.find(username);

    log.info("알림 조회 완료");
    return ResponseEntity.ok(notificationDtos);
  }

  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> delete(
      @PathVariable UUID notificationId,
      @AuthenticationPrincipal UserPrincipal userPrincipal // 우선 이렇게... 내가 구현한 게 아니라서 어떻게 되는지 모름
  ) {
    String username = userPrincipal.getName();

    log.info("알림 삭제(확인) 시도");
    notificationService.delete(notificationId, username);

    return ResponseEntity.noContent().build();
  }
}
