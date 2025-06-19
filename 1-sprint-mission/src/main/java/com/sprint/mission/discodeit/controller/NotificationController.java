package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.NotificationService;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
  private final BasicUserService userService;

  @GetMapping
  public ResponseEntity<List<NotificationDto>> getNotifications(
      @AuthenticationPrincipal UserPrincipal principal
  ) {
    String username = principal.getName();
    UUID userId = userService.findByUserName(username);

    if (userId == null) {
      throw new NoSuchElementException();
    }

    List<NotificationDto> notifications = notificationService.getUserNotifications(userId);
    return ResponseEntity.ok(notifications);
  }

  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> deleteNotification(
      @PathVariable UUID notificationId,
      @AuthenticationPrincipal UserPrincipal principal
  ) {
    String username = principal.getName();
    UUID userId = userService.findByUserName(username);

    if (userId == null) {
      throw new NoSuchElementException();
    }
    notificationService.deleteNotification(notificationId, userId);
    return ResponseEntity.noContent().build();
  }
}
