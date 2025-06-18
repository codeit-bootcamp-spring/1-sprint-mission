package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.channel.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDto {

  UUID id;
  ChannelType type;
  String name;
  String description;
  List<UserDto> participants;
  Instant lastMessageAt;

}
