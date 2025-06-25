package com.sprint.mission.discodeit.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.controller.api.NotificationApi;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController implements NotificationApi {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> findAllByReceiverId(
        @AuthenticationPrincipal DiscodeitUserDetails principal) {
        UUID receiverId = principal.getUserDto().id();
        log.info("알림 목록 조회 요청: receiverId={}", receiverId);
        List<NotificationDto> notifications = notificationService.findAllByReceiverId(receiverId);
        log.debug("알림 목록 조회 응답: count={}", notifications.size());
        return ResponseEntity.ok(notifications);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> delete(
        @AuthenticationPrincipal DiscodeitUserDetails principal,
        @PathVariable UUID notificationId) {
        UUID receiverId = principal.getUserDto().id();
        log.info("알림 삭제 요청: id={}, receiverId={}", notificationId, receiverId);
        notificationService.delete(notificationId, receiverId);
        log.debug("알림 삭제 응답: id={}", notificationId);
        return ResponseEntity.noContent().build();
    }
} 