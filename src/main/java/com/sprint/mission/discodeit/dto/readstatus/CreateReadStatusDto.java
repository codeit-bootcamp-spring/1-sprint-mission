package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public record CreateReadStatusDto(
    @NotBlank(message = "channelId must be specified")
    String channelId,
    @NotBlank(message = "userId must be specified")
    String userId,
    Instant lastReadAt
) {

}
