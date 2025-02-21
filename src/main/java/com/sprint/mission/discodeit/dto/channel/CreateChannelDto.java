package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import jakarta.validation.constraints.*;

public record CreateChannelDto(
    String name,
    String description
) {
}
