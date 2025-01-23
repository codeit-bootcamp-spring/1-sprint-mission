package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private static final String MESSAGE_JSON_FILE = "tmp/messages.json";
    private final ObjectMapper mapper;
    private Map<UUID, Message> messageMap;

    public FileMessageRepository() {
        mapper = new ObjectMapper();
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
    public Message findById(UUID messageId) {
        return loadMessageFromJson().get(messageId);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(loadMessageFromJson().values());
    }

    @Override
    public void delete(UUID messageId) {
        messageMap = loadMessageFromJson();
        if (messageMap.containsKey(messageId)) {
            messageMap.remove(messageId);
            saveMessageToJson(messageMap);
        }
    }

    private Map<UUID, Message> loadMessageFromJson() {
        Map<UUID, Message> map = new HashMap<>();
        File file = new File(MESSAGE_JSON_FILE);
        if (!file.exists()) {
            return map;
        }
        try {
            map = mapper.readValue(file, new TypeReference<Map<UUID, Message>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void saveMessageToJson(Map<UUID, Message> messageMap) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(MESSAGE_JSON_FILE), messageMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
