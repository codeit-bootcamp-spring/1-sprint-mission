package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.message.NullMessageContentException;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.validate.MessageServiceValidator;
import com.sprint.mission.discodeit.service.validate.ServiceValidator;
import com.sprint.mission.discodeit.service.validate.UserServiceValidator;

import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private static final EntityFactory entityFactory = BaseEntityFactory.getInstance();

    private final ServiceValidator<Message> validator = new MessageServiceValidator();
    private final ServiceValidator<User> userValidator = new UserServiceValidator();

    public JCFMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public JCFMessageService() {
        this.messageRepository = new JCFMessageRepository();
    }

    @Override
    public Message createMessage(String title, String content, User sender, User receiver) {
        if (validator.isNullParam(title, content)) {
            throw new NullMessageContentException();
        }
        User senduser = userValidator.entityValidate(sender);
        User receiveUser = userValidator.entityValidate(receiver);

        Message message = entityFactory.createMessage(title, content, senduser, receiveUser);
        return messageRepository.saveMessage(message);
    }

    @Override
    public Message getMessageById(UUID messageId) {
        return validator.entityValidate(messageRepository.findMessageById(messageId));
    }

    @Override
    public Map<UUID, Message> getAllMessages() {
        return validator.entityValidate(messageRepository.findAllMessage());
    }

    @Override
    public Message updateMessage(UUID messageId, String newTitle, String newContent) {
        if (validator.isNullParam(newTitle, newContent)) {
            throw new NullMessageContentException();
        }

        Message message = getMessageById(messageId);
        message.updateMessage(newTitle, newContent);

        return messageRepository.saveMessage(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message findMessage = getMessageById(messageId);
        messageRepository.removeMessageById(findMessage.getMessageId());

        messageRepository.saveMessage(findMessage);
    }
}
