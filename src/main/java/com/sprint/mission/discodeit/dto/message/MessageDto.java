package com.sprint.mission.discodeit.dto.message;


import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  private String content;
  private UUID channelId;
  private UserDto author;
  private List<BinaryContentDto> attachments;
}