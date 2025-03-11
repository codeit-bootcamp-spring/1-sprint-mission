package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

public record MessageCreateRequest(
    String content,
    Channel channel,
    User author
) {

}
