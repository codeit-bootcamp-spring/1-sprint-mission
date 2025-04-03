package com.sprint.mission.discodeit.dto.readstatus;

import java.util.UUID;

public record ReadStatusRequest
    (UUID channelId,
     UUID userId) {

}