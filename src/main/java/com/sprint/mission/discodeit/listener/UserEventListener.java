package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.event.UpdateUserRoleEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final KafkaTemplate<String, UpdateUserRoleEvent> kafkaTemplate;

    @TransactionalEventListener
    public void createNotification(UpdateUserRoleEvent event) {
        kafkaTemplate.send("user", event);
    }
}
