package com.sprint.mission.discodeit.service.proxy;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.UUID;

public class MessageServiceProxy implements MessageService {
    private final MessageService messageService;

    public MessageServiceProxy(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Message createMessage(Message messageToCreate) {
        return messageService.createMessage(messageToCreate);
    }

    @Override
    public Message findMessageById(UUID key) {
        return messageService.findMessageById(key);
    }

    @Override
    public Message updateMessageById(UUID key, Message messageToUpdate) {
        return messageService.updateMessageById(key, messageToUpdate);
    }

    @Override
    public Message deleteMessageById(UUID key) {
        return messageService.deleteMessageById(key);
    }
}
