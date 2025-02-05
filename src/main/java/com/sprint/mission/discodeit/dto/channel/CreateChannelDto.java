package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import jakarta.validation.constraints.*;

public record CreateChannelDto(
    @NotBlank
    @Size(min = 2, max = 10)
    String channelName,
    @NotNull
    Channel.ChannelType channelType,
    @NotBlank
    String serverId,
    @Min(1) @Max(50)
    int maxNumberOfPeople
) {
}
