package com.sprint.mission.repository;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface MessageRepository {

    Message save(Message message);

    Message findById(UUID id);
    List<Message> findAll();
    void delete(UUID messageId);

    boolean existsById(UUID id);
}
