package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.dto.data.UserDto;
import java.util.UUID;

public record MessageCreateRequest(
    String content,
    UUID channelId,
    UUID authorId
) {

}
