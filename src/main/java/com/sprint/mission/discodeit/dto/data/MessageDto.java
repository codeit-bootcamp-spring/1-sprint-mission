package com.sprint.mission.discodeit.dto.data;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto (
  UUID id,
  Instant createdAt,
  Instant updatedAt,
  String content,

  @NotNull
  UUID channelId,

  UUID authorId,
  List<BinaryContentDto> attachments
){

}
