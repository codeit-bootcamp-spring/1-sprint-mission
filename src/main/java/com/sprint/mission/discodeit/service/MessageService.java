package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Optional<Message> createMessage(UUID authorID, UUID channelID, String text);

    Map<UUID, Message> getMessages();

    Optional<Message> getMessage(UUID uuid);

    Optional<Message> updateMessage(UUID uuid, String text);

    Optional<Message> deleteMessage(UUID uuid);
}
