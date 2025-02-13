package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public record PublicChannelCreateRequest(
        // 채널 명
        String name,
        // 채널 설명
        String description
) {
}
