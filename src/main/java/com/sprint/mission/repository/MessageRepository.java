package com.sprint.mission.repository;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public interface MessageRepository {

    Message create(Message message);

    Message findById(UUID id);
    Set<Message> findAll();
    Set<Message> findMessagesInChannel(Channel channel);

    void delete(UUID messageId);
}
