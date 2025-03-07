package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Message;

public class MessageMapper {

  public static MessageResponse toDto(Message message) {
    return new MessageResponse(
        message.getId(),
        message.getText(),
        UserResponse.fromEntity(message.getAuthor()),
        message.getChannel().getId());
  }
}
