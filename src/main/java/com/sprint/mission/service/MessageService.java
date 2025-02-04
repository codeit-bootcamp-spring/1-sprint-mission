package com.sprint.mission.service;


import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public interface MessageService {
    Message createOrUpdate(Message message) throws IOException;
    Message update(UUID messageId, String newMassage);
    Message findById(UUID messageId);
    Set<Message> findAll();
    Set<Message> findMessagesInChannel(Channel channel);
    void delete(UUID messageId);
}
