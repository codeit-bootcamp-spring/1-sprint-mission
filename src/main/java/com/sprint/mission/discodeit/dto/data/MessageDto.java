package com.sprint.mission.discodeit.dto.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  private String content;

  private UUID channelId;
  private UserDto author;

  private List<BinaryContentDto> attachments;
}
