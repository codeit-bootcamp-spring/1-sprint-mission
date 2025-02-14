package com.sprint.mission.discodeit.dto;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class ReadStatusDto {
    UUID userId;
    UUID channelId;
}
