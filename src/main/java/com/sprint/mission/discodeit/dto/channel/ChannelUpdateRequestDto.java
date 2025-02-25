package com.sprint.mission.discodeit.dto.channel;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ChannelUpdateRequestDto {

  private String newName;
  private String NewDescription;

  public ChannelUpdateRequestDto(String newName, String newDescription) {
    this.newName = newName;
    NewDescription = newDescription;
  }
}
