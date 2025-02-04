package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.MessageRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FileMessageRepository implements MessageRepository {
    private final Path directory;
    private final AtomicLong idGenerator = new AtomicLong(0);

    public FileMessageRepository(@Qualifier("fileMessageStoragePath") Path directory) {
        this.directory = directory;
    }

    @PostConstruct  // 의존성 주입이 완료된 후 실행
    private void init() {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                log.info("[Message] Message storage directory created: {}", directory.toAbsolutePath());
            } catch (IOException e) {
                throw new CustomException(ErrorCode.FAILED_TO_CREATE_DIRECTORY);
            }
        }
    }

    @Override
    public Long saveMessage(Message message) {
        Long id = idGenerator.getAndIncrement();
        Path filePath = directory.resolve(id + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(message);
            return id;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_TO_SAVE_DATA);
        }
    }

    @Override
    public Message loadMessage(Long id) {
        Path filePath = directory.resolve(id + ".ser");
        if (!Files.exists(filePath)) {
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new CustomException(ErrorCode.FAILED_TO_LOAD_DATA);
        }
    }

    @Override
    public Map<Long, Message> loadAllMessages() {
        try {
            return Files.list(directory)
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            Message message = (Message) ois.readObject();
                            Long id = Long.valueOf(path.getFileName().toString().replace(".ser", ""));
                            return Map.entry(id, message);
                        } catch (IOException | ClassNotFoundException e) {
                            throw new CustomException(ErrorCode.FAILED_TO_LOAD_DATA);
                        }
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMessage(Long id) {
        try {
            Files.deleteIfExists(directory.resolve(id + ".ser"));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_TO_DELETE_DATA);
        }
    }

    @Override
    public void updateMessage(Long id, Message message) {
        Path filePath = directory.resolve(id + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_TO_UPDATE_DATA);
        }
    }
}
