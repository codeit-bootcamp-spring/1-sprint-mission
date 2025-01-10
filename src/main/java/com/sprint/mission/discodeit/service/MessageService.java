package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(String title, String content, User sender, User receiver);

    Message getMessageById(UUID messageId);

    List<Message> getAllMessages();

    Message updateMessage(UUID messageId, String newTitle, String newContent);

    void deleteMessage(UUID messageId);
}
