package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

    Message save(Message message);

    Optional<Message> findById(UUID messageId);

    List<Message> findAll();

    void deleteById(UUID messageId);

    boolean existsById(UUID messageId);

//    void deleteAllMessagesForChannel(UUID id);
}
