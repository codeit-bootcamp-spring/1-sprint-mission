package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validation.MessageValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data; // assume that it is repository
    private final MessageValidator   messageValidator;

    private JCFMessageService() {
        data             = new HashMap<>();
        messageValidator = MessageValidator.getInstance();
    }

    private static final class InstanceHolder {
        private final static JCFMessageService INSTANCE = new JCFMessageService();
    }

    public static JCFMessageService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Message createMessage(Message messageToCreate) {
        messageValidator.validateBaseEntityFormat(messageToCreate);
        messageValidator.validateContentFormat(messageToCreate);

        UUID key = messageToCreate.getId();
        return Optional.ofNullable(data.putIfAbsent(key, messageToCreate))
                .map(existingMessage -> Message.createEmptyMessage())
                .orElse(messageToCreate);
    }

    @Override
    public Message findMessageById(UUID key) {
        return Optional.ofNullable(data.get(key))
                .orElse(Message.createEmptyMessage());
    }

    @Override
    public Message updateMessageById(UUID key, Message messageToUpdate) {
        Message existingMessage = findMessageById(key);
        messageValidator.validateMessageIdEquality(existingMessage, messageToUpdate);
        messageValidator.validateBaseEntityFormat(messageToUpdate);
        messageValidator.validateContentFormat(messageToUpdate);

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
