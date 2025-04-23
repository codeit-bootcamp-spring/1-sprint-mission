package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record UpdateChannelDto(
    @Size(max = 20)
    String newName,
    @Size(max = 100)
    String newDescription,
    @NotNull
    Instant updatedAt
) {

}
