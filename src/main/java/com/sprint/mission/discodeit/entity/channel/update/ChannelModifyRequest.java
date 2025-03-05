package com.sprint.mission.discodeit.entity.channel.update;

import jakarta.validation.constraints.NotBlank;

public record ChannelModifyRequest(
    @NotBlank
    String newName,

    @NotBlank
    String newDescription
) {

}
