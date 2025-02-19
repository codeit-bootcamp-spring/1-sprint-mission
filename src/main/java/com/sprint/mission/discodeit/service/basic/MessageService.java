package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageService.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.dto.MessageService.MessageResponseDTO;
import com.sprint.mission.discodeit.dto.MessageService.MessageUpdateRequestDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequestDTO requestDTO);
    Message find(UUID messageId);
    List<MessageResponseDTO> findAllByChannelId(UUID channelId);
    MessageResponseDTO update(MessageUpdateRequestDTO request);
    void delete(UUID messageId);
}
