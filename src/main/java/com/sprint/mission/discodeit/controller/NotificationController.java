package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

  @GetMapping
  public List<NotificationDto> findNotifications(@AuthenticationPrincipal UserDto userDto) {
    return notificationService.getNotificationsByUserId(userDto.id());
  }

  @DeleteMapping("/{notificationId}")
  public void deleteNoti(@AuthenticationPrincipal UserDto userDto,
      @PathVariable UUID notificationId) {
    notificationService.deleteNotification(notificationId, userDto.id());
  }

}

