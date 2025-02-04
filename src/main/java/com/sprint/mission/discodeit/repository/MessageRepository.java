package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Map;
import java.util.UUID;

public interface MessageRepository {

    Message save(Message message);
    Map<UUID, Message> load();
    void delete(UUID id);

}
