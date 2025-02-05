package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.file.FileService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileMessageRepository implements MessageRepository {

    private final Path messageDirectory;

    public FileMessageRepository() {
        this.messageDirectory = Paths.get(System.getProperty("user.dir")).resolve("data/message");
        FileService.init(messageDirectory);
    }

    @Override
    public Message save(Message message) {
        Path messagePath = messageDirectory.resolve(message.getId().concat(".ser"));
        FileService.save(messagePath, message);
        return message;
    }

    @Override
    public boolean delete(Message message) {
        Path messagePath = messageDirectory.resolve(message.getId().concat(".ser"));
        return FileService.delete(messagePath);
    }

    @Override
    public Message findById(String id) {
        return findAll().stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Message> findAll() {
        return FileService.load(messageDirectory);
    }
}
