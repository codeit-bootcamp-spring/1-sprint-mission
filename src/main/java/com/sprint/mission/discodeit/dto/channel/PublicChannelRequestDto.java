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
  private UUID ownerId = UUID.fromString("371ab422-6362-4d73-90fb-13d85b178250");

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
