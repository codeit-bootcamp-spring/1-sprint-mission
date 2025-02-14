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
    public boolean delete(String messageId) {
        Path messagePath = messageDirectory.resolve(messageId.concat(".ser"));
        return FileService.delete(messagePath);
    }

    @Override
    public Message findById(String id) {
        Path messagePath = messageDirectory.resolve(id.concat(".ser"));
        List<Message> load = FileService.load(messagePath);
        if (load == null || load.isEmpty()) {
            return null;
        }
        return load.get(0);
    }

    @Override
    public List<Message> findAll() {
        return FileService.load(messageDirectory);
    }

    @Override
    public List<Message> findAllByChannelId(String channelId) {
        return findAll().stream().filter(m -> m.getChannelId().equals(channelId)).toList();
    }
}
