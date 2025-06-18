package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("")
    public ResponseEntity<List<NotificationDto>> findAllNotifications(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(notificationService.findAllNotifications(userDetails.getUserDto()
            .id()));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
        @PathVariable UUID notificationId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        notificationService.deleteNotification(notificationId, userDetails.getUserDto().id());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
