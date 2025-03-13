package com.sprint.mission.discodeit.dto.readstatus;

import java.util.UUID;
public record ReadStatusCreateDTO
        (UUID channelId,
         UUID userId)
{
}