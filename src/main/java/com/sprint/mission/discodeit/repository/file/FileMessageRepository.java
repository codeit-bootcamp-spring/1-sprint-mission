package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.util.FileIOUtil.*;
import static com.sprint.mission.discodeit.util.FileIOUtil.saveToFile;

@Profile("file")
@Repository
public class FileMessageRepository implements MessageRepository {
    private final Path filePath;
    private final Map<UUID, Message> messageMap;

    public FileMessageRepository(@Value("${discodeit.repository.message-file-path}") Path filePath) {
        this.filePath = filePath;
        if (!Files.exists(this.filePath)) {
            try {
                Files.createFile(this.filePath);
                saveToFile(new HashMap<>(), this.filePath);
            } catch (IOException e) {
                throw new RuntimeException("메시지 파일을 초기화 하던 중에 문제가 발생했습니다", e);
            }
        }
        this.messageMap = loadFromFile(this.filePath);
    }

    @Override
    public Message save(Message message) {
        messageMap.put(message.getId(), message);
        saveToFile(messageMap, filePath);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID uuid) {
        return Optional.of(messageMap.get(uuid));
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return messageMap.values().stream()
                .filter(message -> message.getChannelID().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Message> findLatestByChannelId(UUID channelId) {
        return messageMap.values().stream()
                .filter(message -> message.getChannelID().equals(channelId))
                .max(Comparator.comparing(Message::getCreatedAt));
    }

    @Override
    public List<Message> findAll() {
        return messageMap.values().stream().toList();
    }

    @Override
    public Message delete(Message message) {
        messageMap.remove(message.getId());
        saveToFile(messageMap, filePath);
        return message;
    }
}
