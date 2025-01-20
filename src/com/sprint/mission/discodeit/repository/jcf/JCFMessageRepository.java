package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        data = new HashMap<>();
    }
    /**
     * Create the Message while ignoring the {@code createAt} and {@code updateAt} fields from {@code messageInfoToCreate}
     */
    @Override
    public Message createMessage(Message messageInfoToCreate) {
        Message messageToCreate = Message.createMessage(
                messageInfoToCreate.getId(),
                messageInfoToCreate.getContent()
        );

        return Optional.ofNullable(data.putIfAbsent(messageToCreate.getId(), messageToCreate))
                .map(existingMessage -> Message.createEmptyMessage())
                .orElse(messageToCreate);
    }

    @Override
    public Message findMessageById(UUID key) {
        return Optional.ofNullable(data.get(key))
                .orElse(Message.createEmptyMessage());
    }

    /**
     * Update the Message while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields from {@code messageInfoToUpdate}
     */
    @Override
    public Message updateMessageById(UUID key, Message messageInfoToUpdate) {
        Message existingMessage = findMessageById(key);
        Message messageToUpdate = Message.createMessage(
                key,
                existingMessage.getCreateAt(),
                messageInfoToUpdate.getContent()
        );

        return Optional.ofNullable(data.computeIfPresent(
                        key, (id, message) -> messageToUpdate))
                .orElse(Message.createEmptyMessage());
    }

    @Override
    public Message deleteMessageById(UUID key) {
        return Optional.ofNullable(data.remove(key))
                .orElse(Message.createEmptyMessage());
    }
}
