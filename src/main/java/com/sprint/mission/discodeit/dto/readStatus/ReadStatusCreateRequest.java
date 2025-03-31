package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record ReadStatusCreateRequest(
    @NotNull(message = "user 필드는 반드시 값이 필요합니다.")
    User user,
    @NotNull(message = "channel 필드는 반드시 값이 필요합니다,")
    Channel channel,
    @NotNull(message = "lastReadAt 필드는 반드시 값이 필요합니다.")
    Instant lastReadAt
) {

}
