package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private final String filePath = "channels.dat";
    private final Map<UUID, Channel> data = new HashMap<>();

    public FileChannelRepository() {
        loadData();
    }

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
        saveData();
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, Channel updatedChannel) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("Channel with ID " + id + " does not exist.");
        }
        data.put(id, updatedChannel);
        saveData();
    }

    @Override
    public void delete(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("Channel with ID " + id + " does not exist.");
        }
        data.remove(id);
        saveData();
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save channels data.", e);
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
                data.putAll((Map<UUID, Channel>) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load channels data.", e);
        }
    }
}
