package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;


// todo: 프론트랑 맞춰보려고 수정한 것
@Builder
@Jacksonized
public class PublicChannelRequestDto {

  @NotNull
  private String name;

  private String description;

  @NotNull
  @Builder.Default
  private UUID ownerId = UUID.fromString("a579c0ea-6891-4c88-a690-29dac73ff775");

  public String serverName() {
    return this.name;
  }

  public String description() {
    return this.description;
  }

  public UUID ownerId() {
    return this.ownerId;
  }

}

//public record PublicChannelRequestDto(@NotNull String serverName, String description, @NotNull UUID ownerId) {
//}
