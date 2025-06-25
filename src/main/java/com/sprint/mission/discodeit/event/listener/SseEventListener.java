package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.event.BinaryContentStatusChangedEvent;
import com.sprint.mission.discodeit.event.ChannelChangedEvent;
import com.sprint.mission.discodeit.event.MultipleNotificationCreatedEvent;
import com.sprint.mission.discodeit.event.PrivateChannelCreatedEvent;
import com.sprint.mission.discodeit.event.UserChangedEvent;
import com.sprint.mission.discodeit.event.notification.NotificationEvent;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.SseService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEventListener {

  private final SseService sseService;
  private final NotificationService notificationService;

  // NotificationEvent 리스너 - 알림 생성 요청
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async
  public void handleNotificationEvent(NotificationEvent event) {
    try {
      // BasicNotificationService를 사용하여 알림 생성
      notificationService.create(
          event.getReceiverId(),
          event.getTitle(),
          event.getContent(),
          event.getType(),
          event.getTargetId()
      );

      // 생성된 알림을 조회하여 SSE로 전송
      // 최신 알림을 가져오기 위해 목록 조회 후 첫 번째 항목 사용
      List<NotificationDto> notifications = notificationService.findAllByReceiverId(
          event.getReceiverId());
      if (!notifications.isEmpty()) {
        NotificationDto latestNotification = notifications.get(0);
        log.debug("새 알림 SSE 전송: userId={}, notificationId={}, type={}",
            event.getReceiverId(), latestNotification.id(), event.getType());

        sseService.sendNotificationEvent(event.getReceiverId(), latestNotification);
      }

    } catch (Exception e) {
      log.error("알림 생성 및 SSE 전송 실패: receiverId={}", event.getReceiverId(), e);
    }
  }

  // MultipleNotificationCreatedEvent 리스너 - 여러 사용자에게 알림 생성 시
  @EventListener
  public void handleMultipleNotificationCreatedEvent(MultipleNotificationCreatedEvent event) {
    event.receiverIds().forEach(receiverId -> {
      try {
        List<NotificationDto> notifications = notificationService.findAllByReceiverId(receiverId);
        if (!notifications.isEmpty()) {
          NotificationDto latestNotification = notifications.get(0);
          log.debug("새 알림 SSE 전송 (bulk): userId={}, notificationId={}",
              receiverId, latestNotification.id());

          sseService.sendNotificationEvent(receiverId, latestNotification);
        }
      } catch (Exception e) {
        log.error("알림 SSE 전송 실패 (bulk): receiverId={}", receiverId, e);
      }
    });
  }

  // PrivateChannelCreatedEvent 리스너 - 채널 생성 시 참여자들에게 채널 갱신 이벤트 전송
  @EventListener
  public void handlePrivateChannelCreatedEvent(PrivateChannelCreatedEvent event) {
    UUID channelId = event.channel().id();

    // 모든 참여자에게 채널 갱신 이벤트 전송
    event.participantIds().forEach(userId -> {
      log.debug("Private 채널 생성 SSE 전송: userId={}, channelId={}", userId, channelId);
      sseService.sendChannelRefreshEvent(userId, channelId);
    });
  }

  // 파일 업로드 상태 변경 이벤트 리스너
  @EventListener
  public void handleBinaryContentStatusChangedEvent(BinaryContentStatusChangedEvent event) {
    BinaryContentDto binaryContent = event.getBinaryContent();
    UUID userId = event.getUserId();

    log.debug("파일 업로드 상태 변경 SSE 전송: userId={}, binaryContentId={}, status={}",
        userId, binaryContent.id(), binaryContent.uploadStatus());

    sseService.sendBinaryContentStatusEvent(userId, binaryContent);
  }

  // 채널 변경 이벤트 리스너 (채널 수정, 삭제 등)
  @EventListener
  public void handleChannelChangedEvent(ChannelChangedEvent event) {
    UUID channelId = event.getChannelId();

    // 채널과 관련된 모든 사용자에게 전송
    event.getAffectedUserIds().forEach(userId -> {
      log.debug("채널 갱신 SSE 전송: userId={}, channelId={}", userId, channelId);
      sseService.sendChannelRefreshEvent(userId, channelId);
    });
  }

  // 사용자 정보 변경 이벤트 리스너
  @EventListener
  public void handleUserChangedEvent(UserChangedEvent event) {
    UUID changedUserId = event.getUserId();

    // 해당 사용자와 관련된 모든 사용자에게 전송
    event.getAffectedUserIds().forEach(userId -> {
      log.debug("사용자 갱신 SSE 전송: userId={}, changedUserId={}",
          userId, changedUserId);
      sseService.sendUserRefreshEvent(userId, changedUserId);
    });
  }
}