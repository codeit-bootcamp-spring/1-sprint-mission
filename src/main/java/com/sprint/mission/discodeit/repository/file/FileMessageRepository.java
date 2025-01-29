package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    private final String FILE_NAME = "src/main/java/serialized/messages.ser";
    private final Map<UUID, Message> data;

    public FileMessageRepository() {
        this.data = loadData();
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        saveData();
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean update(UUID id, String updatedContent) {
        boolean updated = data.computeIfPresent(id, (key, m) -> {
            m.update(updatedContent);
            return m;
        }) != null;
        if (updated) {
            saveData();
        }
        return updated;
    }

    @Override
    public boolean delete(UUID id) {
        boolean deleted = data.remove(id) != null;
        if (deleted) {
            saveData();
        }
        return deleted;
    }

    private Map<UUID, Message> loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ConcurrentHashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
