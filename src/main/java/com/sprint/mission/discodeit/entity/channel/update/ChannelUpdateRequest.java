package com.sprint.mission.discodeit.entity.channel.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChannelUpdateRequest(
    UUID id,

    @NotBlank
    String channelName,

    @NotBlank
    String channelType,

    @NotNull
    String ownerName

) {

}
