package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelRequest(
    
    @NotBlank(message = "채널 이름을 입력해야합니다.")
    @Size(min = 1, max = 20, message = "채널 이름은 20자 이하여야합니다.")
    String name,

    @Size(max = 100, message = "채널 설명은 100자 이하여야합니다.")
    String description

) {

}
