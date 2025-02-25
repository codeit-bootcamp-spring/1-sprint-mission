package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Repository
@ConditionalOnProperty(name="discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileMessageRepository implements MessageRepository {

    private final Path directory;
    private final String extension;

    public FileMessageRepository(RepositoryProperties properties) {
        this.directory = Paths.get(System.getProperty(properties.getBaseDirectory()))
                .resolve(properties.getFileDirectory())
                .resolve("message");
        this.extension = properties.getExtension();
        FileService.init(this.directory);
    }

    @Override
    public Message save(Message message) {
        Path messagePath = directory.resolve(message.getId().concat(extension));
        FileService.save(messagePath, message);
        return message;
    }

    @Override
    public boolean delete(String messageId) {
        Path messagePath = directory.resolve(messageId.concat(extension));
        return FileService.delete(messagePath);
    }

    @Override
    public Message findById(String id) {
        Path messagePath = directory.resolve(id.concat(extension));
        return (Message) FileService.read(messagePath);
    }

    @Override
    public List<Message> findAll() {
        return FileService.load(directory);
    }

    @Override
    public List<Message> findAllByChannelId(String channelId) {
        return findAll().stream().filter(m -> m.getChannelId().equals(channelId)).toList();
    }
}
