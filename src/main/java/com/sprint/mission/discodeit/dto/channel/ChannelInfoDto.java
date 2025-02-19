package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value(staticConstructor = "of")
public class ChannelInfoDto {
    UUID id;
    Instant createdAt;
    Instant updatedAt;

    ChannelType type;
    String name;
    String description;

    Instant latestMessageTime;
    List<UUID> userIdList;
}
