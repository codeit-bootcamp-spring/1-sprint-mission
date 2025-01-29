package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
    private final String FILE_NAME = "src/main/java/serialized/channels.ser";
    private final Map<UUID, Channel> data;

    public FileChannelRepository() {
        this.data = loadData();
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        saveData();
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean update(UUID id, String name, String topic, ChannelType type) {
        boolean updated = data.computeIfPresent(id, (key, c) -> {
            c.update(name, topic, type);
            return c;
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

    private Map<UUID, Channel> loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) ois.readObject();
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
