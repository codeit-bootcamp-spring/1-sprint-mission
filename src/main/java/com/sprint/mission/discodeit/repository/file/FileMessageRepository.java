package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
//@Primary
public class FileMessageRepository implements MessageRepository {
    private static final String FILE_PATH = "tmp/message.ser";
    private final FileManager<Message> fileManager =  new FileManager<>(FILE_PATH);

    public Message save(Message message) {
        Map<UUID, Message> savedMessageList = loadMessageMapToFile();
        savedMessageList.put(message.getId(), message);
        saveMessageMapToFile(savedMessageList);
        return message;
    }

    public Optional<Message> findById(UUID id) {
        Map<UUID, Message> savedMessageList = loadMessageMapToFile();
        return Optional.ofNullable(savedMessageList.get(id));
    }

    public List<Message> findAll() {
        return fileManager.loadListToFile();
    }

    public List<Message> findAllByChannelId(UUID channelId) {
        return fileManager.loadListToFile().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    public void deleteById(UUID id) {
        Map<UUID, Message> savedMessageList = loadMessageMapToFile();
        savedMessageList.remove(id);
        saveMessageMapToFile(savedMessageList);
    }

    public void deleteAllByChannelId(UUID channelId) {
        Map<UUID, Message> savedMessageList = loadMessageMapToFile();
        for (UUID id : savedMessageList.keySet()) {
            if (savedMessageList.get(id).getChannelId().equals(channelId)) {
                savedMessageList.remove(id);
            }
        }
        saveMessageMapToFile(savedMessageList);
    }

    private void saveMessageMapToFile(Map<UUID, Message> saveMessageMap) {
        List<Message> saveMessageList = saveMessageMap.values().stream().collect(Collectors.toList());
        fileManager.saveListToFile(saveMessageList);
    }

    private Map<UUID, Message> loadMessageMapToFile() {
        List<Message> loadMessageList = fileManager.loadListToFile();
        if (loadMessageList.isEmpty()) {
            return new HashMap<>();
        }
        return loadMessageList.stream()
                .collect(Collectors.toMap(Message::getId, Function.identity()));
    }
}
