package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);

    Optional<Message> findById(UUID uuid);

    List<Message> findAll();

    Message delete(Message message);
}
