package com.spirnt.mission.discodeit.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "수정할 Channel 정보")
public class PublicChannelUpdateRequest {

  private String newName;
  private String newDescription;

  public PublicChannelUpdateRequest(String newName, String newDescription) {
    this.newName = newName;
    this.newDescription = newDescription;
  }
}
