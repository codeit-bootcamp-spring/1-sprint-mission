package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.notification.NotificationEvent;
import com.sprint.mission.discodeit.event.notification.NotificationEventPublisher;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class BasicNotificationService implements NotificationService {
  private final NotificationRepository notificationRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final NotificationEventPublisher notificationEventPublisher;

  //새로운 메시지에 대한 알림 활성화 여부
  @Override
  public List<NotificationDto> getMyNotifications(UUID userId) {
    log.info("알람 조회 시작: userId={}", userId);
    List<NotificationDto> notifications = new ArrayList<>();
    List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
    User user = userRepository.findById(userId).orElseThrow();

    for (ReadStatus readStatus : readStatuses) {
      Channel channel = readStatus.getChannel();
      if (channel.getUpdatedAt().isAfter(readStatus.getUpdatedAt())) {

        // 중복 저장 방지 예시
        boolean exists = notificationRepository.existsByReceiverIdAndTargetIdAndType(
            user, channel.getId(), NotificationType.NEW_MESSAGE);
        if (exists) continue;

        notificationEventPublisher.publish(new NotificationEvent(
            userId,
            "읽지 않은 메시지가 있습니다",
            "채널 [" + channel.getName() + "]에 새 메시지가 있습니다.",
            NotificationType.NEW_MESSAGE,
            channel.getId()
        ));
        //응답용 DTO // 추후 알람 목록에서 다시 id 조회해서 id 값을 알아야 함
        notifications.add(NotificationDto.builder()
            .id(null)
            .title("읽지 않은 메시지가 있습니다")
            .content("채널 [" + channel.getName() + "]에 새 메시지가 있습니다.")
            .createdAt(Instant.now())
            .type(NotificationType.NEW_MESSAGE)
            .targetId(channel.getId())
            .receiverId(userId)
            .build());
      }
    }

    return notifications;
  }


  @Override
  public void deleteMyNotification(UUID notificationId, UUID userId) throws AccessDeniedException {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> NotificationNotFoundException.withId(notificationId));

    if (!notification.getReceiverId().getId().equals(userId)) {
      throw new AccessDeniedException("해당 알림에 대한 권한이 없습니다.");
    }

    notificationRepository.delete(notification);
    log.info("알림 삭제 완료: notificationId={}, userId={}", notificationId, userId);
  }

}
