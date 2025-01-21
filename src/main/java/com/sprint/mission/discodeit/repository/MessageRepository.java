package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface MessageRepository {

    void save(Message message);

    Message findById(UUID uuid);

    Map<UUID, Map<UUID, Message>> findAll();

    void delete(UUID uuid);

    void deleteAllMessagesForChannel(UUID channelId);
}
