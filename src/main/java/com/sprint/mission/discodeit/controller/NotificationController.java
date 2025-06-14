package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 조회
    @GetMapping
    public ResponseEntity<NotificationDto> findAll() {

        return ResponseEntity.ok(null);
    }

    // 알림 삭제 (확인)
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID notificationId) {

        return ResponseEntity.notFound().build();
    }
}
