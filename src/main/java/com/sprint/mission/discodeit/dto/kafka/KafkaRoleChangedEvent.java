package com.sprint.mission.discodeit.dto.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.dto.event.RoleChangedEvent;
import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;

public record KafkaRoleChangedEvent(
    @JsonProperty("userId") UUID userId,
    @JsonProperty("oldRole") Role oldRole,
    @JsonProperty("newRole") Role newRole,
    @JsonProperty("topic") String topic
) {

    public static KafkaRoleChangedEvent from(
        RoleChangedEvent event, String topic
    ) {
        return new KafkaRoleChangedEvent(
            event.userId(),
            event.oldRole(),
            event.newRole(),
            topic
        );
    }
}