package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BaseRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.file.FileService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BasicMessageService implements MessageService{
    private final MessageRepository repository;

    public BasicMessageService(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Message create(Message message) {
        repository.save(message);
        return message;
    }

    @Override
    public Message readOne(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Message> readAll() {
        return repository.readAll();
    }

    @Override
    public Message update(UUID id, Message message) {
        return repository.modify(id, message);
    }

    @Override
    public boolean delete(UUID id) {
        return repository.deleteById(id);
    }

    @Override
    public List<Message> readAll(UUID id) {
        return null;
    }
}
