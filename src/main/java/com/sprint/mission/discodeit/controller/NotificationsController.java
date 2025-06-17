package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.NotificationApi;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import java.nio.file.AccessDeniedException;
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

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/notificationsController")
public class NotificationsController implements NotificationApi {
  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<List<NotificationDto>> getNotifications(@AuthenticationPrincipal DiscodeitUserDetails user) { //엔티티를 받는다?
    List<NotificationDto> notifications = notificationService.getMyNotifications(user.getId());
    return ResponseEntity.ok(notifications);
  }
  @DeleteMapping("/{notificationDtoId}")
  public ResponseEntity<Void> deleteNotification(@AuthenticationPrincipal DiscodeitUserDetails user, @PathVariable UUID notificationId)
      throws AccessDeniedException {
    notificationService.deleteMyNotification(notificationId, user.getId());
    return ResponseEntity.noContent().build();
  }

}
