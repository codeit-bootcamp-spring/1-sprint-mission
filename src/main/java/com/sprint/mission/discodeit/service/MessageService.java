package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageDto createMessage(MessageCreateDTO messageCreateDTO,
      List<BinaryContentCreateDTO> attachmentRequests);

  MessageDto findById(UUID id);

  List<MessageDto> findAllByChannelId(UUID channelId);


  MessageDto update(UUID id, MessageUpdateDTO messageUpdateDTO);

  void deleteMessage(UUID msgID);


}
