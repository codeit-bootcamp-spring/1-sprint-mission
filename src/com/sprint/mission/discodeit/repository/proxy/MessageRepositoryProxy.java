package com.sprint.mission.discodeit.repository.proxy;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.log.ServiceLogger;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.UUID;

public class MessageRepositoryProxy implements MessageRepository {
    private final ServiceLogger logger;
    private final MessageRepository messageRepository;

    public MessageRepositoryProxy(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        logger = ServiceLogger.getInstance();
    }

    @Override
    public Message createMessage(Message messageInfoToCreate) {
        Message creation   = messageRepository.createMessage(messageInfoToCreate);
        String  logMessage = "Message creation failed";
        UUID    messageId  = messageInfoToCreate.getId();

        if (creation == Message.EMPTY_MESSAGE) {
            logger.warning(logMessage, messageId);
        }

        return creation;
    }

    @Override
    public Message findMessageById(UUID key) {
        Message find = messageRepository.findMessageById(key);

        if (find == Message.EMPTY_MESSAGE) {
            logger.warning("Message find failed", key);
        }

        return find;
    }

    @Override
    public Message updateMessageById(UUID key, Message messageInfoToUpdate) {
        Message updated    = messageRepository.updateMessageById(key, messageInfoToUpdate);
        String  logMessage = "Message update failed";

        if (updated == Message.EMPTY_MESSAGE) {
            logger.warning(logMessage, key);
        }

        return updated;
    }

    @Override
    public Message deleteMessageById(UUID key) {
        Message deletion = messageRepository.deleteMessageById(key);

        if (deletion == Message.EMPTY_MESSAGE) {
            logger.warning("Message deletion failed", key);
        }

        return deletion;
    }
}

