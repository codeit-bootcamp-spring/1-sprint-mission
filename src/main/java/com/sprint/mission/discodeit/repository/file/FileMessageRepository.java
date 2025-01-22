package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.FileStorage;
import com.sprint.mission.discodeit.service.file.SerializableFileStorage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository{

    private static final Path ROOT_DIR = Paths.get(System.getProperty("user.dir"), "tmp");
    private static final String MESSAGE_FILE = "message.ser";
    private final FileStorage<Message> fileStorage;

    public FileMessageRepository() {
        this.fileStorage = new SerializableFileStorage<>(Message.class);
        fileStorage.init(ROOT_DIR);
    }

    @Override
    public Message save(Message message) {
        List<Message> messages = findAll();
        messages.add(message);
        fileStorage.save(ROOT_DIR.resolve(MESSAGE_FILE), messages);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return findAll().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Message> findAll() {
        return fileStorage.load(ROOT_DIR);
    }

    @Override
    public void delete(UUID id) {
        List<Message> messages = findAll();
        messages.removeIf(m -> m.getId().equals(id));
        fileStorage.save(ROOT_DIR.resolve(MESSAGE_FILE), messages);
    }

    @Override
    public void deleteByChannel(Channel channel) {
        List<Message> messages = findAll();
        messages.removeIf(message -> message.getChannel().equals(channel));
        fileStorage.save(ROOT_DIR.resolve(MESSAGE_FILE), messages);
    }

    @Override
    public void deleteByUser(User user) {
        List<Message> messages = findAll();
        messages.removeIf(message -> message.getAuthor().equals(user));
        fileStorage.save(ROOT_DIR.resolve(MESSAGE_FILE), messages);
    }
}
