package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;


@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository extends FileRepository implements MessageRepository {

    public FileMessageRepository(@Value("${discodeit.repository.message}") String fileDirectory) {
        super(fileDirectory);
    }
    @Override
    public void save(Message message) {
        Path path = resolvePath(message.getId());
        saveToFile(path,message);

    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        Path path = resolvePath(messageId);
        return loadFromFile(path);
    }

    @Override
    public Map<UUID, Message> findAll() {
        Map<UUID, Message> messageMap = new HashMap<>();
        try (Stream<Path> pathStream = Files.walk(getDIRECTORY())) {
            pathStream.filter(path -> path.toString().endsWith(".ser"))
                    .forEach(path -> {
                        Optional<Message> message = loadFromFile(path);
                        message.ifPresent(msg -> messageMap.put(msg.getId(), msg));
                    });
        } catch (IOException e) {
            System.out.println("파일을 읽을 수 없습니다." + e.getMessage());
        }
        return messageMap;
    }

    @Override
    public void delete(UUID messageId) {

    }

    @Override
    public boolean existsById(UUID messageId) {
        return false;
    }
}