package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(MessageRequest.Create request);
    List<Message> findAllByChannelId(UUID channelId);
    Message searchById(UUID id);
    Message findByIdOrThrow(UUID id);
    void updateMessage(UUID id, MessageRequest.Update request);
    void deleteMessage(UUID id);
    void deleteAllByChannelId(UUID id);
}
