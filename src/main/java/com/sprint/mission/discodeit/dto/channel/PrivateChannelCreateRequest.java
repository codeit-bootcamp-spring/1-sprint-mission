package com.sprint.mission.discodeit.dto.channel;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
        // 참여자
        List<UUID> participants
) {
}
