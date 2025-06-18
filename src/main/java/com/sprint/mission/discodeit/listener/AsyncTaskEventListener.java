package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.event.AsyncTaskFailureEvent;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AsyncTaskEventListener {

    private final NotificationService notificationService;

    @TransactionalEventListener
    public void createNotification(AsyncTaskFailureEvent event) {
        notificationService.createNotification(
            event.notificationType(),
            event.taskName(),
            event.username()
        );
    }

}
