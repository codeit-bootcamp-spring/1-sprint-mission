package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;

public interface CreateMessageFacade {
  MessageResponseDto createMessage(CreateMessageDto messageDto, String channelId);
}
