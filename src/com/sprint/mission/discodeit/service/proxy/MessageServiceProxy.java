package com.sprint.mission.discodeit.service.proxy;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.log.ServiceLogger;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.UUID;

public class MessageServiceProxy implements MessageService {
    private final ServiceLogger logger;
    private final MessageService messageService;

    public MessageServiceProxy(MessageService messageService) {
        this.messageService = messageService;
        logger = ServiceLogger.getInstance();
    }

    @Override
    public Message createMessage(Message messageInfoToCreate) {
        Message creation   = Message.createEmptyMessage();
        String  logMessage = "Message creation failed";
        UUID    messageId  = messageInfoToCreate.getId();

        try {
            creation = messageService.createMessage(messageInfoToCreate);
        } catch (InvalidFormatException e) {
            logger.warning(e.getErrorCode(), logMessage, messageId);
        }

        return creation;
    }

    @Override
    public Message findMessageById(UUID key) {
        return messageService.findMessageById(key);
    }

    @Override
    public Message updateMessageById(UUID key, Message messageInfoToUpdate) {
        Message updated    = Message.createEmptyMessage();
        String  logMessage = "Message update failed";

        try {
            updated = messageService.updateMessageById(key, messageInfoToUpdate);
        } catch (InvalidFormatException e) {
            logger.warning(e.getErrorCode(), logMessage, key);
        }

        return updated;
    }

    @Override
    public Message deleteMessageById(UUID key) {
        return messageService.deleteMessageById(key);
    }
}
