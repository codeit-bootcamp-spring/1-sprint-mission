package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Optional<Message> findMessageById(UUID id);

    List<Message> findMessagesBySender(UUID senderId);

    List<Message> findAllMessages();

    void save(Message message);

    void remove(UUID id);
}
