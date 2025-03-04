package com.spirnt.mission.discodeit.repository.file;

import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository extends FileRepository implements MessageRepository {
    public FileMessageRepository(@Value("${discodeit.repository.Message}") String fileDirectory) {
        super(fileDirectory);
    }

    @Override
    public void save(Message message) {
        Path path = resolvePath(message.getId());
        saveToFile(path, message);
    }

    @Override
    public void delete(UUID messageId) {
        Path path = resolvePath(messageId);
        deleteFile(path);
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        Path path = resolvePath(messageId);
        return loadFromFile(path);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return this.findAll().values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Map<UUID, Message> findAll() {
        Map<UUID, Message> messages = new HashMap<>();
        // 폴더 내 모든 .ser 파일을 찾음
        try (Stream<Path> paths = Files.walk(this.getDirectory())) {
            paths.filter(path -> path.toString().endsWith(".ser"))  // .ser 파일만 필터링
                    .forEach(path -> {
                        Optional<Message> messageOptional = loadFromFile(path);
                        messageOptional.ifPresent(message -> messages.put(message.getId(), message));
                    });
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }

        return messages;
    }

    @Override
    public boolean existsById(UUID userId) {
        Path path = resolvePath(userId);
        return Files.exists(path);
    }
}
