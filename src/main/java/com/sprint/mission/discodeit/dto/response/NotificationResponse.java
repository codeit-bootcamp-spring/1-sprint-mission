package com.sprint.mission.discodeit.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sprint.mission.discodeit.entity.Notification.NotificationType;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
@JsonInclude(Include.NON_NULL)
public record NotificationResponse(
    UUID id,
    Instant createdAt,
    UUID receiverId,
    String title,
    String content,
    NotificationType type,
    UUID targetId
) {

}
