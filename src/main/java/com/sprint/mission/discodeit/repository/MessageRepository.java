package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);

    Message findByMessageId(UUID messageId);

    List<Message> findbyUsername(String username);

    List<Message> findAll();

    void deleteById(UUID id);
}
