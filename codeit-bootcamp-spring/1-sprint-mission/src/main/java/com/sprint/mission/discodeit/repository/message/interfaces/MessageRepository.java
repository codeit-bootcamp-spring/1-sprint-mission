package com.sprint.mission.discodeit.repository.message.interfaces;

import com.sprint.mission.discodeit.domain.message.Message;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

    Message save(Message message);

    Optional<Message> findById(UUID uuid);

    void deleteById(UUID uuid);
}
