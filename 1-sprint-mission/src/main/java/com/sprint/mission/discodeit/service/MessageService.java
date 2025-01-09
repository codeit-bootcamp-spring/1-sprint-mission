package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(Message message, Channel channel);

    Message read(UUID messageId);

    List<Message> readAll();

    void update(UUID messageId, String messageContent);

    void delete(UUID messageId, Channel channel);
}
