package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validation.MessageValidator;

import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageValidator  messageValidator;

    public JCFMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        messageValidator       = MessageValidator.getInstance();
    }

    /**
     * Create the Message while ignoring the {@code createAt} and {@code updateAt} fields from {@code messageInfoToCreate}
     */
    @Override
    public Message createMessage(Message messageInfoToCreate) throws InvalidFormatException {
        validateFormat(messageInfoToCreate);

        return messageRepository.createMessage(messageInfoToCreate);
    }

    @Override
    public Message findMessageById(UUID key) {
        return messageRepository.findMessageById(key);
    }

    /**
     * Update the Message while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields from {@code messageInfoToUpdate}
     */
    @Override
    public Message updateMessageById(UUID key, Message messageInfoToUpdate) throws InvalidFormatException {
        validateFormat(messageInfoToUpdate);

        return messageRepository.updateMessageById(key, messageInfoToUpdate);
    }

    private void validateFormat(Message messageInfoToUpdate) throws InvalidFormatException {
        messageValidator.validateIdFormat(messageInfoToUpdate);
        messageValidator.validateCreateAtFormat(messageInfoToUpdate);
        messageValidator.validateUpdateAtFormat(messageInfoToUpdate);
        messageValidator.validateContentFormat(messageInfoToUpdate);
    }

    @Override
    public Message deleteMessageById(UUID key) {
        return messageRepository.deleteMessageById(key);
    }
}
