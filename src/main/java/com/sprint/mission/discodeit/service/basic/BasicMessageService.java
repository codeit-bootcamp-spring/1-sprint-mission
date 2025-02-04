package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.message.NullMessageTitleException;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.validate.MessageServiceValidator;
import com.sprint.mission.discodeit.service.validate.ServiceValidator;
import com.sprint.mission.discodeit.service.validate.UserServiceValidator;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private static final EntityFactory entityFactory = BaseEntityFactory.getInstance();

    private final MessageRepository messageRepository;

    private final ServiceValidator<Message> validator = new MessageServiceValidator();
    private final ServiceValidator<User> userValidator = new UserServiceValidator();

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @Override
    public Message createMessage(String title, String content, User sender, User receiver) {
        if (validator.isNullParam(title)) {
            throw new NullMessageTitleException();
        }

        User sendUser = userValidator.entityValidate(sender);
        User receiveUser = userValidator.entityValidate(receiver);

        Message message = entityFactory.createMessage(title, content, sendUser, receiveUser);
        return messageRepository.saveMessage(message);
    }

    @Override
    public Message getMessageById(UUID messageId) {
        Map<UUID, Message> messages = messageRepository.findAllMessage();
        return validator.entityValidate(messages.get(messageId));
    }

    @Override
    public Map<UUID, Message> getAllMessages() {
        return Optional.ofNullable(messageRepository.findAllMessage())
                .orElseThrow(MessageNotFoundException::new);
    }

    @Override
    public Message updateMessage(UUID messageId, String newTitle, String newContent) {
        if (messageId == null) {
            throw new MessageNotFoundException();
        } else if (validator.isNullParam(newTitle, newContent)) {
            throw new MessageNotFoundException();
        }

        Message findMessage = getMessageById(messageId);

        findMessage.updateMessage(newTitle, newContent);
        messageRepository.saveMessage(findMessage);

        return findMessage;
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message findMessage = getMessageById(messageId);

        messageRepository.removeMessageById(findMessage.getMessageId());
    }
}
