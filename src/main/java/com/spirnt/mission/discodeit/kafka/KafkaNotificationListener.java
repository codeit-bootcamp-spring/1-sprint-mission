package com.spirnt.mission.discodeit.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spirnt.mission.discodeit.async.notification.NotificationCreateEvent;
import com.spirnt.mission.discodeit.entity.NotificationType;
import com.spirnt.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * KafkaListener 흐름
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaNotificationListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "discodeit.new-message", groupId = "notification-group")
    public void handleNewMessage(String message) {
        processMessage(message, NotificationType.NEW_MESSAGE);
    }

    @KafkaListener(topics = "discodeit.role-changed", groupId = "notification-group")
    public void handleRoleChanged(String message) {
        processMessage(message, NotificationType.ROLE_CHANGED);
    }

    @KafkaListener(topics = "discodeit.async-failed", groupId = "notification-group")
    public void handleAsyncFailed(String message) {
        processMessage(message, NotificationType.ASYNC_FAILED);
    }

    private void processMessage(String message, NotificationType expectedType) {
        try {
            NotificationCreateEvent event = objectMapper.readValue(message,
                NotificationCreateEvent.class);
            if (!event.type().equals(expectedType)) {
                log.warn("Wrong input expected={}, actual={}", expectedType,
                    event.type());
                return;
            }

            notificationService.create(event);
            log.info("Kafka Message Processing Completed: {}", event);

        } catch (Exception e) {
            log.error("Kafka Message Processing Failed", e);
        }
    }
}
