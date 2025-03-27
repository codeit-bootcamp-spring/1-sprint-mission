package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageDto createMessage(MessageCreateDTO messageCreateDTO);

  MessageDto findById(UUID id);

  List<MessageDto> findAllByChannelId(UUID channelId);


  MessageDto update(MessageUpdateDTO messageUpdateDTO);

  void deleteMessage(UUID msgID);


}
