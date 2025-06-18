package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.basic.NotificationService;
import java.util.List;
import java.util.Map;
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
  private final UserRepository userRepository;
  
  @GetMapping
  public ResponseEntity<List<NotificationDto>> getNotifications(
      @AuthenticationPrincipal DiscodeitUserDetails principal
  ) {
    UUID receiverId = principal.getUserDto().id();

    User user = userRepository.findById(receiverId)
        .orElseThrow(() -> new UserNotFoundException(Map.of("receiverId", receiverId)));
    log.info("알림 조회 시도");
    List<NotificationDto> notificationDtos = notificationService.find(user.getId());

    log.info("알림 조회 완료");
    return ResponseEntity.ok(notificationDtos);
  }


  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> delete(
      @PathVariable UUID notificationId,
      @AuthenticationPrincipal DiscodeitUserDetails principal
  ) {
    UUID receiverId = principal.getUserDto().id();
    User user = userRepository.findById(receiverId)
        .orElseThrow(() -> new UserNotFoundException(Map.of("receiverId", receiverId)));

    log.info("알림 삭제(확인) 시도");
    notificationService.delete(notificationId, user.getId());

    return ResponseEntity.noContent().build();
  }
}
