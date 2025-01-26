package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final FileMessageRepository repository;

    public FileMessageService() {
        this.repository = new FileMessageRepository();
    }

    @Override
    public void addMessage(Message message) {
        repository.save(message);
    }

    @Override
    public Message getMessage(UUID id) {
        Message message = repository.findById(id);
        if (message == null) {
            throw new IllegalArgumentException("존재하지 않는 메시지입니다.");
        }
        return message;
    }

    @Override
    public List<Message> getAllMessages() {
        return repository.findAll();
    }

    @Override
    public void updateMessage(UUID id, String newContent) {
        Message message = repository.findById(id);
        if (message == null) {
            throw new IllegalArgumentException("존재하지 않는 메시지입니다.");
        }
        message.setContent(newContent);
        repository.update(id, message);
    }

    @Override
    public void deleteMessage(UUID id) {
        if (repository.findById(id) == null) {
            throw new IllegalArgumentException("존재하지 않는 메시지입니다.");
        }
        repository.delete(id);
    }
}
