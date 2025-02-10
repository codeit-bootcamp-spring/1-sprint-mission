package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.constant.MessageType;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Message extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  private final UUID channelId;
  private final UUID senderId;
  private UUID replyToId;
  private String content;
  @Nullable
  private List<BinaryContent> binaryContent;
  private MessageType type;
  
  public static Message ofCommon(UUID channelId, UUID senderId, List<BinaryContent> binaryContent, String content) {
    return new Message(channelId, senderId, null, content, binaryContent, MessageType.COMMON);
  }
  
  public static Message ofReply(UUID channelId, UUID senderId, UUID replyToId, List<BinaryContent> binaryContent, String content) {
    return new Message(channelId, senderId, replyToId, content, binaryContent, MessageType.REPLY);
  }
  
  public void updateContent(String content) {
    this.content = content;
    super.update();
  }
}
