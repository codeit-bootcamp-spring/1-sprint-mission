package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class PublicChannelCreateRequest {

  @NotBlank(message = "channel name is required")
  private String name;
  private String description;

  public PublicChannelCreateRequest(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
