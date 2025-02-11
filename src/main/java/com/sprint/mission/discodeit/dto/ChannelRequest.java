package com.sprint.mission.discodeit.dto;

import java.util.List;

public record ChannelRequest(
        String name,
        String description,
        List<UserResponse> member,
        UserResponse owner
) {
}
