package com.sprint.mission.discodeit.dto.readstatus;

import lombok.Getter;

import java.util.UUID;
@Getter

public record ReadStatusCreateDTO
        (UUID channelId,
         UUID userId)
{
}