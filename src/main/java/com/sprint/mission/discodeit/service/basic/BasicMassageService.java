package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;


import java.util.*;

public class BasicMassageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMassageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @Override
    public void createMessage(Message message) {
        validateMessage(message);
        messageRepository.createMessage(message);
    }

    @Override
    public Optional<Message> getMessageById(UUID id) {
        return Optional.ofNullable(messageRepository.getMessageById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not fount")));

    }

    @Override
    public List<Message> getAllMessage() {
        return messageRepository.getAllMessage();
    }

    @Override
    public void updateMessage(UUID id, Message updatedMessage) {
        messageRepository.updateMessage(id, updatedMessage);
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.deleteMessage(id);
    }

    private void validateMessage(Message message) {
        if (message.getContent() == null || message.getContent().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }
        if (message.getContent().length() > 255) {
            throw new IllegalArgumentException("Message content cannot exceed 255 characters");
        }
    }
}
