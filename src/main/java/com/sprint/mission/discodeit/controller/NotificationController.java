package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
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
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<List<NotificationDto>> getNotifications(
      @AuthenticationPrincipal DiscodeitUserDetails userDetails
  ) {
    log.info("알림 목록 조회 요청: userId = {}", userDetails.getUserDto().id());
    UUID userId = userDetails.getUserDto().id();
    log.debug("알림 목록 조회 요청 반환: userId = {}", userId);
    return ResponseEntity.ok(notificationService.getNotifications(userId));
  }

  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> deleteNotifications(
      @PathVariable UUID notificationId,
      @AuthenticationPrincipal DiscodeitUserDetails userDetails
  ) {
    log.info("알림 삭제 요청: notificationId = {}, userId = {}", notificationId,
        userDetails.getUserDto().id());
    UUID userId = userDetails.getUserDto().id();
    notificationService.deleteNotification(notificationId);
    log.debug("알림 삭제 요청 처리 완료: userId = {}", userId);
    return ResponseEntity.noContent().build();
  }

}
