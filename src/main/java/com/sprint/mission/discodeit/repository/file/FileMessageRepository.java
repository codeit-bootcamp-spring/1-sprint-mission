package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {

    private final FileStorage fileStorage;
    private final Path directory;
    private static final String SUBDIRECTORY = "message";

    public FileMessageRepository(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
        directory = fileStorage.getDirectory(SUBDIRECTORY);
        fileStorage.init(directory);
    }

    @Override
    public Message saveMessage(Message message) {
        Path filePath = directory.resolve(message.getId().toString().concat(".ser"));
        return fileStorage.save(filePath, message);
    }

    @Override
    public Message findMessageById(UUID messageId) {
        List<Message> messageList = fileStorage.load(directory);

        return messageList.stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst().orElse(null);
    }

    @Override
    public List<Message> findAllMessages() {
        return fileStorage.load(directory);
    }

    @Override
    public List<Message> findAllMessagesByChannel(Channel channel) {
        List<Message> messageList = fileStorage.load(directory);

        return messageList.stream()
                .filter(message -> message.getChannel().equals(channel))
                .toList();
    }

    @Override
    public Optional<Message> findLastMessage() {
        return Optional.empty();
    }

    @Override
    public void removeMessage(UUID messageId) {
        Message message = findMessageById(messageId);
        fileStorage.remove(directory, message);
    }
}
