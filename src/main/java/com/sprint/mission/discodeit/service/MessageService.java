package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID userId, UUID channelId, String content);
    Message getMessage(UUID id);
    List<Message> getAllMessages();
    Message updateMessage(UUID id, String content);
    void deleteMessage(UUID id);
}
