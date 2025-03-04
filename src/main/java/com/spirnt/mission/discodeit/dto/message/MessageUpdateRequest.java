package com.spirnt.mission.discodeit.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "수정할 Message 내용")
public class MessageUpdateRequest {

  private String newContent;

  public MessageUpdateRequest(String newContent) {
    this.newContent = newContent;
  }
}