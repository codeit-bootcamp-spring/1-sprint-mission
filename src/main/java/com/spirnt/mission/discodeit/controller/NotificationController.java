package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.dto.notification.NotificationDto;
import com.spirnt.mission.discodeit.security.CustomUserDetails;
import com.spirnt.mission.discodeit.service.NotificationService;
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
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("")
    public ResponseEntity<List<NotificationDto>> findAll(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<NotificationDto> result = notificationService.findAll(userDetails.getUser().getId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> delete(@PathVariable UUID notificationId) {
        notificationService.delete(notificationId);
        return ResponseEntity.noContent().build();
    }
}
