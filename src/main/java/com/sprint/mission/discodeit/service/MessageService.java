package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageDTO.CreateMessageDTO createMessageDTO);
    Message find(UUID messageId);
    List<Message> findAll();
    List<Message> findAllByChannelId(UUID channelId);
    Message update(UUID messageId, MessageDTO.UpdateMessageDTO updateMessageDTO);
    void delete(UUID messageId);
}
