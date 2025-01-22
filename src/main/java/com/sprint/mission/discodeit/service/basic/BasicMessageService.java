package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository repository;

    public BasicMessageService(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createMessage(Message message) {
        repository.save(message);
        System.out.println("[Message Created]: " + message);
    }

    @Override
    public Optional<Message> getMessage(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Message> getAllMessages() {
        return repository.findAll();
    }

    @Override
    public void updateMessage(Message message) {
        repository.save(message);
        System.out.println("[Message Updated]: " + message);
    }

    @Override
    public void deleteMessage(UUID id) {
        repository.delete(id);
        System.out.println("[Message Deleted]");
    }
}