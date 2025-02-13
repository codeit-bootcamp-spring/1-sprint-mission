package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.config.FileConfig;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
public class FileMessageRepository implements MessageRepository {
    private final String messageJsonFile;
    private final ObjectMapper mapper;
    private Map<UUID, Message> store;

    @Autowired
    public FileMessageRepository(FileConfig fileConfig) {
        String fileDirectory = fileConfig.getFileDirectory();
        String fileName = fileConfig.getMessageJsonPath();
        this.messageJsonFile = fileDirectory + "/" + fileName;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        store = loadMessageFromJson();
    }

    @Override
    public Message save(Message message) {
        store.put(message.getId(), message);
        saveMessageToJson(store);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return new ArrayList<>(store.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList());
    }

    @Override
    public void deleteById(UUID id) {
        if (store.remove(id) != null) {
            saveMessageToJson(store);
        }
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        store.values().removeIf(message -> message.getChannelId().equals(channelId));
        saveMessageToJson(store);
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

    private void saveMessageToJson(Map<UUID, Message> store) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(messageJsonFile), store);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
    }
}
