package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository repository;
    private static volatile BasicMessageService instance;

    public BasicMessageService(MessageRepository repository) {
        this.repository = repository;
    }
    public static BasicMessageService getInstance(MessageRepository repository) {
        if (instance == null) {
            synchronized (BasicMessageService.class) {
                if (instance == null) {
                    instance = new BasicMessageService(repository);
                }
            }
        }
        return instance;
    }
    @Override
    public UUID createMessage(UUID id, String content) {
        return repository.save(id, content);
    }

    @Override
    public Message getMessage(UUID id) {
        return repository.findMessageById(id);
    }

    @Override
    public List<Message> getMessagesByUserId(UUID userId) {
        return repository.findMessagesById(userId);
    }

    @Override
    public List<Message> getMessages() {
        return repository.findAll();
    }

    @Override
    public void updateMessage(UUID id, String content) {
        repository.update(id, content);
    }

    @Override
    public void deleteMessage(UUID id) {
        repository.delete(id);
    }
}
