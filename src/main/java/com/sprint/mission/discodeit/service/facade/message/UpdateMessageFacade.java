package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;

public interface UpdateMessageFacade {
  MessageResponseDto updateMessage(String messageId, MessageUpdateDto messageDto);
}
