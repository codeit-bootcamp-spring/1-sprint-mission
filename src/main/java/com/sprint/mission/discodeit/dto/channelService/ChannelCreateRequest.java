package com.sprint.mission.discodeit.dto.channelService;

public record ChannelCreateRequest(
        String name,
        String description,
        String password
) {
}
