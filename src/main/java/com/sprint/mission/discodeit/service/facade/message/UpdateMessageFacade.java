package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;

public interface UpdateMessageFacade {
  MessageResponseDto updateMessage(String messageId, MessageUpdateDto messageDto);
}
