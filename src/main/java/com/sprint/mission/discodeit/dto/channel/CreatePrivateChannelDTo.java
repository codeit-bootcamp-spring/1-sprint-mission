package com.sprint.mission.discodeit.dto.channel;

import java.util.List;

public record CreatePrivateChannelDTo(
        CreateChannelDto createChannelDto,
        List<String> userIds
) {
}
