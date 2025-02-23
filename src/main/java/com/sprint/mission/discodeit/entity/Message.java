package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.constant.MessageType;
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
  private List<UUID> binaryContentIds;
  private MessageType type;
  
  public static Message ofCommon(UUID channelId, UUID senderId, List<UUID> binaryContentIds, String content) {
    return new Message(channelId, senderId, null, content, binaryContentIds, MessageType.COMMON);
  }
  
  public static Message ofReply(UUID channelId, UUID senderId, UUID replyToId, List<UUID> binaryContentIds, String content) {
    return new Message(channelId, senderId, replyToId, content, binaryContentIds, MessageType.REPLY);
  }
  
  public void updateContent(String content) {
    this.content = content;
    super.update();
  }
}
