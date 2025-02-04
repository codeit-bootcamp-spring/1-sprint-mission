package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private final String filePath = "messages.dat";
    private Map<UUID, Message> data = new HashMap<>();

    public FileMessageRepository() {
        loadFromFile();
    }

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
        saveToFile();
    }

    @Override
    public Optional<Message> findById(UUID id) {
        loadFromFile();
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll() {
        loadFromFile();
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, Message message) {
        if (data.containsKey(id)) {
            data.put(id, message);
            saveToFile();
        } else {
            throw new IllegalArgumentException("Message not found: " + id);
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Error saving data to file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                data = (Map<UUID, Message>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading data from file, starting fresh: " + e.getMessage());
            }
        }
    }
}
