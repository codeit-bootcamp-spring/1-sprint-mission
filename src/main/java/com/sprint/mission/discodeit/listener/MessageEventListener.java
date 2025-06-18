package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.event.CreateMessageEvent;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MessageEventListener {

    private final NotificationService notificationService;

    @TransactionalEventListener
    public void createNotification(CreateMessageEvent event) {
        notificationService.createNotification(
            event.notificationType(),
            event.channel(),
            event.message()
        );
    }
}
