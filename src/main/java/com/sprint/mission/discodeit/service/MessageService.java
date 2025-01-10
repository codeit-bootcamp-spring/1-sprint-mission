package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageType;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void createMessage(User sender, String content, MessageType type); //이게 전송...?

    Message findMessage(UUID id);

    List<Message> findAllMessages();

    void updateMessage(UUID id, String newContent);

    void removeMessage(UUID id);
}
