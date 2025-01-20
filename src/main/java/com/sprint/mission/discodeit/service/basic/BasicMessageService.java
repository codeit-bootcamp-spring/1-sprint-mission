package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void create(Message message) {
        messageRepository.save(message);
    }

    @Override
    public Optional<Message> read(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> readAll() {
        return messageRepository.findAll();
    }

    @Override
    public void update(UUID id, Message message) {
        if (messageRepository.findById(id).isPresent()) {
            messageRepository.save(message);
        } else {
            throw new IllegalArgumentException("Message not found for ID: " + id);
        }
    }

    @Override
    public void delete(UUID id) {
        messageRepository.delete(id);
    }
}
