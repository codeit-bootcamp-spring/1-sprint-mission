package com.sprint.mission.discodeit.entity.channel.create;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ChannelCreateRequest(
    UUID channelId,

    @NotBlank
    String channelName,

    @NotBlank
    String ownerName,

    @NotBlank
    String type
) {

}
