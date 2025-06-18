package com.sprint.mission.discodeit.dto.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.dto.event.AsyncTaskFailedEvent;
import java.util.UUID;

public record KafkaAsyncTaskFailedEvent(
    @JsonProperty("userId") UUID userId,
    @JsonProperty("taskName") String taskName,
    @JsonProperty("requestId") String requestId,
    @JsonProperty("failureReason") String failureReason,
    @JsonProperty("topic") String topic
) {

    public static KafkaAsyncTaskFailedEvent from(
        AsyncTaskFailedEvent event,
        String topic
    ) {
        return new KafkaAsyncTaskFailedEvent(
            event.userId(),
            event.taskName(),
            event.requestId(),
            event.failureReason(),
            topic
        );
    }
}