package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageRepository implements MessageRepository {
    private final String filePath = "messages.ser";
    private Map<UUID, Message> storage = new HashMap<>();

    public FileMessageRepository() {
        load();
    }

    @Override
    public Message save(Message message) {
        storage.put(message.getId(), message);
        persist();
        return message;
    }

    @Override
    public Message findById(UUID id) {
        return storage.get(id);
    }

    @Override
    public List<Message> findAll() {
        return storage.values().stream().collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
        persist();
    }

    private void persist() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(storage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist message data", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                storage = (Map<UUID, Message>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                storage = new HashMap<>();
            }
        }
    }
}
