package com.sprint.mission.discodeit.dto.channel;

import lombok.Getter;

@Getter
public class PublicChannelUpdateRequest {

  private String newName;
  private String newDescription;

  public PublicChannelUpdateRequest(String newName, String newDescription) {
    this.newName = newName;
    this.newDescription = newDescription;
  }
}
