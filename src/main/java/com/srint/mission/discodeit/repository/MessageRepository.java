package com.srint.mission.discodeit.repository;

import com.srint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {

    UUID save(Message message);
    Message findOne(UUID id);
    List<Message> findAll();
    UUID delete (UUID id);
}
