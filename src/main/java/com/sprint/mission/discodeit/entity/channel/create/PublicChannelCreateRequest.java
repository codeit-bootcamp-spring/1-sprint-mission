package com.sprint.mission.discodeit.entity.channel.create;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest(

    @NotBlank
    String name,

    @NotBlank
    String description
) {

}
