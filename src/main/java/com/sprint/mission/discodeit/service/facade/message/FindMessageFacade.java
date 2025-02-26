package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.MessageResponseDto;

import java.util.List;

public interface FindMessageFacade {
  MessageResponseDto findMessageById(String id);
  List<MessageResponseDto> findMessagesByChannel(String channelId);
}
