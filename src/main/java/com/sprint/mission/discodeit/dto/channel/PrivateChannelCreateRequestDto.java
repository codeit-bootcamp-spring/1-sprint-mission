package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateChannelCreateRequestDto {

  @NotEmpty
  @Size(min = 1)
  private List<UUID> participantIds;
}
