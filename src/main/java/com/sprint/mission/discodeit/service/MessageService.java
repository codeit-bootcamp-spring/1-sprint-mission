package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID id, Long createdAt, Long updatedAt, String content);
    Message getMessage(UUID id);
    List<Message> getAllMessages();
    void updateMessage(UUID id, String content, Long updatedAt);
    void deleteMessage(UUID id);
}
