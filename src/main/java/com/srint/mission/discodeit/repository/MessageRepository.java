package com.srint.mission.discodeit.repository;

import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {

    UUID save(Message message);
    Message findOne(UUID id);
    List<Message> findAll();
    UUID update(Message message);
    UUID delete (UUID id);
}
