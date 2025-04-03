package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ChannelUpdateDTO(
    //TODO: uuid가 필요하지 않을 것 같다.
    UUID uuid,
    @Size(min = 1, max = 20, message = "채널 이름은 20자 이하여야합니다.")
    String name
) {

}
