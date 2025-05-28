package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ChannelResponse(
    UUID id,
    Channel.ChannelType type,
    String name,
    String description,
    List<UserResponse> participants,
    Instant lastMessageTime

) {

//  public static ChannelResponse entityToDto(Channel channel, Instant lastMessageTime,
//      List<UUID> participantIds) {
//    return ChannelResponse.builder()
//        .id(channel.getId())
//        .name(channel.getName())
//        .description(channel.getDescription())
//        .type(channel.getType())
//        .participants(participantIds)
//        .lastMessageTime(lastMessageTime)
//        .build();
//  }
}
