package com.spirnt.mission.discodeit.async.notification;

import com.spirnt.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 비동기 방식 알림 생성 리스너
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListner {

    private final NotificationService notificationService;

    @Async("propagatingExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createNotification(NotificationCreateEvent notificationCreateEvent) {
        notificationService.create(notificationCreateEvent);
    }

}
