package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private final String filePath = "channels.dat";
    private Map<UUID, Channel> data = new HashMap<>();

    public FileChannelRepository() {
        loadFromFile();
    }

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
        saveToFile();
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        loadFromFile();
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAll() {
        loadFromFile();
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, Channel channel) {
        if (data.containsKey(id)) {
            data.put(id, channel);
            saveToFile();
        } else {
            throw new IllegalArgumentException("Channel not found: " + id);
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
                data = (Map<UUID, Channel>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading data from file, starting fresh: " + e.getMessage());
            }
        }
    }
}
