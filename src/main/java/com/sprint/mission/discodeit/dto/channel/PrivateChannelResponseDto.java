package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public record PrivateChannelResponseDto(
    String id,
    Channel.ChannelType type,
    List<String> participantIds
){
}
