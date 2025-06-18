package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.basic.BasicNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final BasicNotificationService notificationService;


    @GetMapping
    public ResponseEntity<List<NotificationDto>> findAll(Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        log.info("알림 인증자 - {}", userDto.getId());
        return ResponseEntity.ok(notificationService.findAllByReceiver(userDto.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
        UserDto userDto = (UserDto) authentication.getPrincipal();
        notificationService.delete(id, userDto.getId());
        return ResponseEntity.noContent().build();
    }
}