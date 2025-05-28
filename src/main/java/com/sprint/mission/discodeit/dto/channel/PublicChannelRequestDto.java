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
  private UUID ownerId = UUID.fromString("abdbdf81-5cb8-4a5a-97e7-d2d839c2bbcf");

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
