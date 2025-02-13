package com.sprint.mission.discodeit.dto.channel;

public record PublicChannelCreateRequest(
        // 채널 명
        String name,
        // 채널 설명
        String description
) {
}
