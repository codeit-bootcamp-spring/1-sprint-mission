package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message createMessage(String text);

    List<Message> getMessages();

    Optional<Message> getMessage(UUID uuid);

    Optional<Message> updateMessage(UUID uuid, String text);

    Optional<Message> deleteMessage(UUID uuid);
}
