package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {

    private final Path directoryPath;
    private final String FILE_EXTENSION = ".ser";

    private final FileManager fileManager;

    public FileMessageRepository(@Value("${discodeit.repository.file-directory}") String directory, FileManager fileManager) {
        this.fileManager = fileManager;
        this.directoryPath = Path.of(System.getProperty("user.dir"), directory, "messages");
    }

    @PostConstruct
    private void init() {
        fileManager.createDirectory(directoryPath);
    }

    @Override
    public Message save(Message message) {
        Path path = directoryPath.resolve(message.getId().toString().concat(FILE_EXTENSION));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(message);
            return message;
        } catch (IOException e) {
            throw new FileIOException("message 저장 실패");
        }
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        Path path = directoryPath.resolve(messageId.toString().concat(FILE_EXTENSION));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            Message message = (Message) ois.readObject();
            return Optional.of(message);
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        File[] files = directoryPath.toFile().listFiles();
        List<Message> messages = new ArrayList<>(100);

        if (files == null) {
            return messages;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Message message = (Message) ois.readObject();
                if (message.getChannel().getId().equals(channelId)) {
                    messages.add(message);
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new FileIOException("messages 읽기 실패");
            }
        }
        return messages;
    }

    @Override
    public void updateMessage(Message message) {
        save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Path path = directoryPath.resolve(messageId.toString().concat(FILE_EXTENSION));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("message 삭제 실패");
            }
        }
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        findByChannelId(channelId)
                .forEach(m -> deleteMessage(m.getId()));
    }
}