package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(MessageRequest.Create request);
    List<Message> findAllByChannelId(UUID channelId);
    Message findById(UUID id);
    Message findByIdOrThrow(UUID id);
    void update(UUID id, MessageRequest.Update request);
    void deleteById(UUID id);
    void deleteAllByChannelId(UUID id);
}
