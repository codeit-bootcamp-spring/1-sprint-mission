package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessageRequestDto {

  @NotNull
  private String content;
  @NotNull
  private UUID channelId;
  @NotNull
  private UUID authorId;
}
