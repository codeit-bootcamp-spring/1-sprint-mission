package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.Size;

public record CreatePublicChannelRequest(

    @Size(max = 100, message = "최대 100자까지 입력할 수 있습니다. 일부 문자는 더 빨리 제한에 도달할 수 있어요.")
    String name,

    @Size(max = 500, message = "최대 500자까지 입력할 수 있습니다. 일부 문자는 더 빨리 제한에 도달할 수 있어요.")
    String description
) {

}
