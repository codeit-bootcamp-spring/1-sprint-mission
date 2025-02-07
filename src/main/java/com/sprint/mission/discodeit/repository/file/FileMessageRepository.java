package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.config.FileConfig;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
@Primary
public class FileMessageRepository implements MessageRepository {
    private final String messageJsonFile;
    private final ObjectMapper mapper;
    private Map<UUID, Message> messageMap;

    @Autowired
    public FileMessageRepository(FileConfig fileConfig) {
        this.messageJsonFile = fileConfig.getMessageJsonPath();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        messageMap = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        messageMap = loadMessageFromJson();
        messageMap.put(message.getId(), message);
        saveMessageToJson(messageMap);
        return message;
    }

    @Override
    public Optional<Message> findByMessageId(UUID messageId) {
        return Optional.ofNullable(loadMessageFromJson().get(messageId));
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return new ArrayList<>(loadMessageFromJson().values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList());
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        messageMap = loadMessageFromJson();
        if (messageMap.containsKey(messageId)) {
            messageMap.remove(messageId);
            saveMessageToJson(messageMap);
        }
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        Map<UUID, Message> messageMap = loadMessageFromJson();
        messageMap.values().removeIf(message -> message.getChannelId().equals(channelId));
        saveMessageToJson(messageMap);
    }

    private Map<UUID, Message> loadMessageFromJson() {
        Map<UUID, Message> map = new HashMap<>();
        File file = new File(messageJsonFile);
        if (!file.exists()) {
            return map;
        }
        try {
            map = mapper.readValue(file, new TypeReference<Map<UUID, Message>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
        return map;
    }

    private void saveMessageToJson(Map<UUID, Message> messageMap) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(messageJsonFile), messageMap);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
    }
}
