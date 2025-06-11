package com.sprint.mission.discodeit.dto.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.dto.event.NotificationEvent;
import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;

public record KafkaNotificationEvent(
    @JsonProperty("receiverId") UUID receiverId,
    @JsonProperty("title") String title,
    @JsonProperty("content") String content,
    @JsonProperty("type") NotificationType type,
    @JsonProperty("targetId") UUID targetId,
    @JsonProperty("topic") String topic
) {

    public static KafkaNotificationEvent from(
        NotificationEvent event,
        String topic
    ) {
        return new KafkaNotificationEvent(
            event.receiverId(),
            event.title(),
            event.content(),
            event.type(),
            event.targetId(),
            topic
        );
    }
}
