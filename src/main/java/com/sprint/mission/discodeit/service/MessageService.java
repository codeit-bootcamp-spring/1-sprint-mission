package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void createCommonMessage(User sender, String content);

    void createReplyMessage(User sender, String content);

    Message findMessageById(UUID id);

    List<Message> findMessageBySender(UUID senderId);

    List<Message> findAllMessages();

    void updateMessage(UUID id, String newContent);

    void removeMessage(UUID id);
}
