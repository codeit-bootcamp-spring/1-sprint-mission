package com.sprint.mission.service;


import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createOrUpdate(Message message) throws IOException;
    Message update(UUID messageId, String newMassage);
    Message findById(UUID messageId);
    List<Message> findAll();
    List<Message> findMessagesInChannel(Channel channel);
    void delete(UUID messageId);
}
