package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.Message;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);

    void delete(UUID messageId);

    Optional<Message> findById(UUID messageId);

    Map<UUID, Message> findAll();

    // 존재 검증
    boolean existsById(UUID messageId);
}
