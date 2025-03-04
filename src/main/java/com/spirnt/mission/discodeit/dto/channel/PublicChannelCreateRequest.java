package com.spirnt.mission.discodeit.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Public Channel 생성 정보")
public class PublicChannelCreateRequest {

  private String name;
  private String description;

  public PublicChannelCreateRequest(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
