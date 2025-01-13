package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(UUID id, String userName, String message);

    Message getMessageId(UUID id);

    List<Message> getAllMessages();

    void updateMessage(UUID id, String newMessage);

    void deleteMessage(UUID id);
}
