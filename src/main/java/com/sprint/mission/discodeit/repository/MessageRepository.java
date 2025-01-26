package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void createMessage(UUID id, Message message);
    void updateMessage(UUID id, String content);
    void deleteMessage(UUID id);
    Optional<Message> findById(UUID id);
    List<Message> findAll();
}
