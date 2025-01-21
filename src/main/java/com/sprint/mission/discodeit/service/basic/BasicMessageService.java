package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.Map;
import java.util.UUID;

public class BasicMessageService implements MessageService {

    private static final EntityFactory entityFactory = BaseEntityFactory.getInstance();

    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessage(String title, String content, User sender, User receiver) {
        Message message = entityFactory.createMessage(title, content, sender, receiver);

        return messageRepository.saveMessage(message);
    }

    @Override
    public Message getMessageById(UUID messageId) {
        return messageRepository.findMessageById(messageId);
    }

    @Override
    public Map<UUID, Message> getAllMessages() {
        return messageRepository.findAllMessage();
    }

    @Override
    public Message updateMessage(UUID messageId, String newTitle, String newContent) {
        Message findMessage = messageRepository.findMessageById(messageId);

        findMessage.updateMessage(newTitle, newContent);
        messageRepository.saveMessage(findMessage);

        return findMessage;
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageRepository.removeMessageById(messageId);
    }
}
