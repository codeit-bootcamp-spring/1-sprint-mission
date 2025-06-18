package com.sprint.mission.discodeit.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHandler {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    @EventListener
    public void handleEvent(NotificationEvent event) {
        try {
            String topic = resolveTopic(event.getType());
            String payload = mapper.writeValueAsString(event);
            kafkaTemplate.send(topic, payload);
            log.info("kafka로 이벤트 전송: topic={}, payload={}", topic, payload);
        } catch (Exception e) {
            log.error("kafka 전송 실패", e);
        }
    }

    private String resolveTopic(NotificationType type) {
        return switch (type) {
            case NEW_MESSAGE -> "discodeit.new-message";
            case ROLE_CHANGE -> "discodeit.role-change";
            case ASYNC_FAILED -> "discodeit.async-failed";
        };
    }
}
