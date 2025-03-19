package com.spirnt.mission.discodeit.dto.channel;

import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChannelDto {

  private UUID id;
  private String name;
  private String description;
  private ChannelType type;
  private List<UserDto> participants;
  private Instant lastMessageAt;

  @Builder
  public ChannelDto(UUID id, String name, String description, ChannelType type,
      List<UserDto> participants, Instant lastMessageAt) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.type = type;
    this.participants = (participants == null) ? new ArrayList<>() : participants;
    this.lastMessageAt = lastMessageAt;
  }

  public static ChannelDto of(Channel channel, List<UserDto> participants, Instant lastMessageAt) {
    return ChannelDto.builder()
        .id(channel.getId())
        .name(channel.getName())
        .description(channel.getDescription())
        .type(channel.getType())
        .participants(participants)
        .lastMessageAt(lastMessageAt)
        .build();
  }
}
