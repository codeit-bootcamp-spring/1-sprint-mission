package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;

public record ReadStatusCreateRequest(
    User user,
    Channel channel,
    Instant lastReadAt
) {

}
