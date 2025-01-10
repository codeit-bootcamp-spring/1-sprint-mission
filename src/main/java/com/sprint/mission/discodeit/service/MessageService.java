package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID sendUserId, UUID channelId, String content);
    Message findMessageById(UUID messageId);
    List<Message> findAllMessage();
    void updateMessage(UUID sendUserId, UUID messageId, String content);
    void deleteMessage(UUID sendUserId, UUID messageId);
}
