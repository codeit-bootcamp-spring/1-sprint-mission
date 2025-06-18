package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @PreAuthorize("#userDetails.user.id == authentication.principal.id")
  @GetMapping
  public ResponseEntity<List<NotificationDto>> getNotifications(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(notificationService.getNotifications(userDetails.getUser().getId()));
  }

  @PreAuthorize("#userDetails.user.id == authentication.principal.id")
  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> deleteNotification(
      @PathVariable UUID notificationId,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    notificationService.deleteNotification(notificationId, userDetails.getUser().getId());
    return ResponseEntity.status(204).build();
  }
}
