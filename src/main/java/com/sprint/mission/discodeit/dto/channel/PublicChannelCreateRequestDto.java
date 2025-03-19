package com.sprint.mission.discodeit.dto.channel;

import lombok.Getter;


@Getter
public class PublicChannelCreateRequestDto {

  private final String name;
  private final String description;

  public PublicChannelCreateRequestDto(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
