package com.sprint.mission.discodeit.notification;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.service.basic.BasicNotificationService;
import com.sprint.mission.discodeit.service.basic.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final BasicNotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final SseService sseService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(NotificationEvent event) {
        try {
            Notification notification = notificationService.send(event);

            sseService.sendNotification(event.receiverId(), notificationMapper.toDto(notification));

        } catch (Exception e) {
            log.error("알림 저장 실패: {}", event, e);
            throw e; // @Retryable을 사용하면 자동 재시도 가능
        }
    }
}