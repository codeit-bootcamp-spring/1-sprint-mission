package com.sprint.mission.discodeit.dto.response;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,

    @NotNull
    UUID channelId,

    UserDto author,
    List<BinaryContentDto> attachments
) {

}
