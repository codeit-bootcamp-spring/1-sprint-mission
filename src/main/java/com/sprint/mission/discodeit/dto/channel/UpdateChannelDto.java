package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record UpdateChannelDto(
    @Size(max = 20)
    String channelName,
    @Size(max = 100)
    String description,
    @NotNull
    Instant updatedAt
) {

}
