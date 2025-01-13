package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validation.MessageValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data; // assume that it is repository
    private final MessageValidator   messageValidator;

    public JCFMessageService() {
        data             = new HashMap<>();
        messageValidator = MessageValidator.getInstance();
    }

    /**
     * Create the Message while ignoring the {@code createAt} and {@code updateAt} fields from {@code messageInfoToCreate}
     */
    @Override
    public Message createMessage(Message messageInfoToCreate) {
        try {
            messageValidator.validateBaseEntityFormat(messageInfoToCreate);
            messageValidator.validateContentFormat(messageInfoToCreate);
        } catch (InvalidFormatException e) {
            System.out.println(e.getMessage());
        }

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

        try {
            messageValidator.validateBaseEntityFormat(messageInfoToUpdate);
            messageValidator.validateContentFormat(messageInfoToUpdate);
        } catch (InvalidFormatException e) {
            System.out.println(e.getMessage());
        }

        Message messageToUpdate = Message.createMessage(
                key,
                existingMessage.getCreateAt(),
                messageInfoToUpdate.getContent()
        );

        return Optional.ofNullable(data.computeIfPresent(
                        key, (id, message)-> messageToUpdate))
                .orElse(Message.createEmptyMessage());
    }

    @Override
    public Message deleteMessageById(UUID key) {
        return Optional.ofNullable(data.remove(key))
                .orElse(Message.createEmptyMessage());
    }
}
