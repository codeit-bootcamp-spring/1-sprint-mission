package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Setter;

@Getter
@Setter
public class ChannelDto {

  private UUID id;
  private ChannelType type;
  private String name;
  private String description;
  private List<UserDto> participants;
  private Instant lastMessageAt;
}
