package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final NotificationMapper notificationMapper;

  public List<NotificationDto> find(String username) {
    log.info("알림 조회 시작");

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(Map.of("username", username)));

    List<Notification> notifications = notificationRepository.findByReceiverId(user.getId());

    return notifications.stream()
        .map(notificationMapper::toDto)
        .toList();
  }

  public void delete(UUID id, String username) {
    log.info("알림 삭제(확인) 시작");

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(Map.of("username", username)));

    Notification notification = notificationRepository.findById(id)
        .orElseThrow(() -> new NotificationNotFoundException(Map.of("notificaitonId", id)));

    if (user.getId() == notification.getReceiverId()) {
      notificationRepository.deleteById(id);
      log.info("알림 삭제(확인) 완료");
    } else {
      log.warn("유저 id와 알림 수신자의 id가 일치하지 않습니다. userId={}, receiverId={}", user.getId(),
          notification.getReceiverId());
    }
  }
}
