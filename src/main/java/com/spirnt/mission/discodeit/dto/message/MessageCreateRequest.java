package com.spirnt.mission.discodeit.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Getter;

@Getter
@Schema(description = "Message 생성 정보")
public class MessageCreateRequest {

  private String content;
  private UUID authorId;
  private UUID channelId;

  public MessageCreateRequest(UUID authorId, UUID channelId,
      String content) {
    this.content = content;
    this.authorId = authorId;
    this.channelId = channelId;
  }
}
