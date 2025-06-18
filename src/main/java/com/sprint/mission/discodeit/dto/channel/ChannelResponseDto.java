package com.sprint.mission.discodeit.dto.channel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
public class ChannelResponseDto {

  private final UUID id;
  private final Channel.ChannelType type;
  private final String name;
  private final String description;
  private final List<UserResponseDto> participants;

  @JsonSerialize(using = InstantSerializer.class)
  @JsonDeserialize(using = InstantDeserializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "UTC")
  private final Instant lastMessageAt;

  @Builder
  private ChannelResponseDto(UUID id, Channel.ChannelType type, String name,
      String description, List<UserResponseDto> participants,
      Instant lastMessageAt) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.description = description;
    this.participants = participants;
    this.lastMessageAt = lastMessageAt;
  }

}
