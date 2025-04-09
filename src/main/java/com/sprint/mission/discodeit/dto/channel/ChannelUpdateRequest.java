package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.Size;

public record ChannelUpdateRequest(
    @Size(min = 1, max = 20, message = "채널 이름은 20자 이하여야합니다.")
    String name
) {

}
