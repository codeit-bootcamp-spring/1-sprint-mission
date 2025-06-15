package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
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

  @GetMapping
  public ResponseEntity<List<NotificationDto>> getMyNotifications(@AuthenticationPrincipal UserPrincipal user) {
    List<NotificationDto> notifications = notificationService.getMyNotifications(user.getId());
    return ResponseEntity.ok(notifications);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteNotification(
      @PathVariable UUID id,
      @AuthenticationPrincipal UserPrincipal user
  ) {
    notificationService.deleteMyNotification(user.getId(), id);
    return ResponseEntity.noContent().build();
  }
}
