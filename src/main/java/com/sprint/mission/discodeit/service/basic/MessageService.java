package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageService.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.dto.MessageService.MessageResponseDTO;
import com.sprint.mission.discodeit.dto.MessageService.MessageUpdateRequestDTO;
import com.sprint.mission.discodeit.dto.binaryContentService.BinaryContentCreateRequestDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequestDTO requestDTO, List<BinaryContentCreateRequestDTO> binaryContentCreateRequests);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    Message update(UUID messageId, MessageUpdateRequestDTO request);
    void delete(UUID messageId);
}
