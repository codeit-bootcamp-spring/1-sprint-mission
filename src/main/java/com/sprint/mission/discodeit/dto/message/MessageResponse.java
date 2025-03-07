package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Message;
import java.util.UUID;

public record MessageResponse(UUID id, String text, UserResponse author, UUID channelId) {

  public static MessageResponse fromEntity(Message message) {
    return new MessageResponse(
        message.getId(),
        message.getText(),
        UserResponse.fromEntity(message.getAuthor()),
        message.getChannel().getId());
  }
}
