package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileMessageRepository implements MessageRepository {
    private static final String FILEPATH = "tmp/message.ser";
    private final FileManager<Message> fileManager =  new FileManager<>(FILEPATH);

    public Message save(Message message) {
        Map<UUID, Message> savedMessageList = loadMessageMapToFile();
        savedMessageList.put(message.getId(), message);
        saveMessageMapToFile(savedMessageList);
        return message;
    }

    public Optional<Message> findById(UUID messageId) {
        Map<UUID, Message> savedMessageList = loadMessageMapToFile();
        return Optional.ofNullable(savedMessageList.get(messageId));
    }

    public List<Message> findAll() {
        return loadMessageListToFile();
    }

    public void delete(UUID messageId) {
        Map<UUID, Message> savedMessageList = loadMessageMapToFile();
        savedMessageList.remove(messageId);
        saveMessageMapToFile(savedMessageList);
    }

    private void saveMessageMapToFile(Map<UUID, Message> saveMessageMap) {
        List<Message> saveMessageList = saveMessageMap.values().stream().toList();
        fileManager.saveListToFile(saveMessageList);
    }

    private List<Message> loadMessageListToFile() {
        return fileManager.loadListToFile();
    }

    private Map<UUID, Message> loadMessageMapToFile() {
        List<Message> loadMessageList = loadMessageListToFile();
        if (loadMessageList.isEmpty()) {
            return new HashMap<>();
        }
        return loadMessageList.stream()
                .collect(Collectors.toMap(Message::getId, Function.identity()));
    }
}
