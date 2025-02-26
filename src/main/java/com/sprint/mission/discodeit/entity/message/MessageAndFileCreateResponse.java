package com.sprint.mission.discodeit.entity.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageAndFileCreateResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UUID authorId,
    List<UUID> attachmentIds

) {

}
