package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void create(Message message);
    void update(UUID id, Message message);
    void delete(UUID id);
    List<Message> readAll();
}
