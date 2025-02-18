package com.sprint.mission.discodeit.dto.readStatus.request;


import java.util.UUID;

public record CreateReadStatusRequest(
        UUID userId,
        UUID channelId
) {
}
