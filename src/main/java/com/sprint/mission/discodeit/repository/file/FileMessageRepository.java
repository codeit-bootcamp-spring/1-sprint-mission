package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private final String filePath = "messages.dat";
    private final Map<UUID, Message> data = new HashMap<>();

    public FileMessageRepository() {
        loadData();
    }

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
        saveData();
    }

    @Override
    public Message findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, Message updatedMessage) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("Message with ID " + id + " does not exist.");
        }
        data.put(id, updatedMessage);
        saveData();
    }

    @Override
    public void delete(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("Message with ID " + id + " does not exist.");
        }
        data.remove(id);
        saveData();
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save messages data.", e);
        }
    }

    private void loadData() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                data.putAll((Map<UUID, Message>) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load messages data.", e);
        }
    }
}
