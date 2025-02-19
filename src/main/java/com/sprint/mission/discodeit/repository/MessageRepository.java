package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {

    UUID save(Message message);
    Message findOne(UUID id);
    List<Message> findAll();
    List<Message> findAllByChannelId(UUID id);
    UUID update(Message message);
    UUID delete (UUID id);
}
