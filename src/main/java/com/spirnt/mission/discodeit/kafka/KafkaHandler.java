package com.spirnt.mission.discodeit.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spirnt.mission.discodeit.async.notification.NotificationCreateEvent;
import com.spirnt.mission.discodeit.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaHandler {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String NOTIFICATION = "discodeit.new-message";
    private static final String CHANGE_ROLE = "discodeit.role-changed";
    private static final String ASYNC_FAILED = "discodeit.async-task-failed";

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(NotificationCreateEvent event) {
        try {
            String topic = resolveTopic(event.type());
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, payload);
            log.info("Kafka 메시지 전송 완료: topic={}, payload={}", topic, payload);
        } catch (Exception e) {
            log.error("Kafka 메시지 전송 실패", e);
        }
    }

    private String resolveTopic(NotificationType type) {
        return switch (type) {
            case NEW_MESSAGE -> NOTIFICATION;
            case ROLE_CHANGED -> CHANGE_ROLE;
            case ASYNC_FAILED -> ASYNC_FAILED;
        };
    }

}
