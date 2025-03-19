package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {

  private UUID id;
  private ChannelType type;
  private String name;
  private String description;
  private List<UserDto> participants;
  private Instant lastMessageAt;
}