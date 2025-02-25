package com.sprint.mission.discodeit.dto.channel;

import java.util.List;
import java.util.UUID;

public record ChannelDto(
    UUID id, String name,
    String description,
    Long lastMessageTime,
    List<UUID> memberIds
) {

}
