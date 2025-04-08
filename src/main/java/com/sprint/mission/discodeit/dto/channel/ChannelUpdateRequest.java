package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChannelUpdateRequest(
    @NotBlank(message = "채널 이름은 공백으로 둘 수 없습니다.")
    @Size(min = 2, max = 30, message = "channelName 길이는 2~30자여야 합니다.")
    String newName,
    String newDescription
) {

}
