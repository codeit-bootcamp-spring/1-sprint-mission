package com.sprint.mission.repository;

import com.sprint.mission.entity.main.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {

    Message save(Message message);

    Message findById(UUID id);
    List<Message> findAll();
    void delete(UUID messageId);

    boolean existsById(UUID id);
}
