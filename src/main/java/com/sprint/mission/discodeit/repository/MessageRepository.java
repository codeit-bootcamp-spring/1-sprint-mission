package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {

    Message save(Message message);

    Message findMessage(UUID messageId);

    List<Message> findAll();

    void updateMessage(Message message);

    void deleteMessage(UUID messageId);
}
