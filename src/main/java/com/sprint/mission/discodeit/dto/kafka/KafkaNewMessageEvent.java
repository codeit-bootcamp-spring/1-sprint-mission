package com.sprint.mission.discodeit.dto.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.dto.event.NewMessageEvent;
import java.util.UUID;

public record KafkaNewMessageEvent(
    @JsonProperty("channelId") UUID channelId,
    @JsonProperty("authorId") UUID authorId,
    @JsonProperty("content") String content,
    @JsonProperty("topic") String topic
) {

    public static KafkaNewMessageEvent from(
        NewMessageEvent event, String topic
    ) {
        return new KafkaNewMessageEvent(
            event.channelId(),
            event.authorId(),
            event.content(),
            topic
        );
    }
}
