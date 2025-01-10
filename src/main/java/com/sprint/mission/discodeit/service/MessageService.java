package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface MessageService {
    void create(Message message);
    Optional<Message> read(UUID id);
    List<Message> readAll();
    void update(UUID id, Message message);
    void delete(UUID id);
}
