package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePublicChannelDto(
    //채널명
    @NotBlank
    String name,
    //채널설명
    @Size(max = 100)
    String description
) {

}
