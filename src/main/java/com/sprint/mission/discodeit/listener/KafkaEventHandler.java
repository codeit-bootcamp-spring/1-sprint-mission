package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.event.UpdateUserRoleEvent;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventHandler {

    private final NotificationService notificationService;

    @KafkaListener(topics = "user", groupId = "discodeit-group")
    public void createNotification(UpdateUserRoleEvent event) {
        notificationService.createNotification(
            event.notificationType(),
            event.user()
        );
    }

}
