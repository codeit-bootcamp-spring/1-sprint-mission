package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileChannelRepository implements ChannelRepository {
    private final String filePath = "channels.ser";
    private Map<UUID, Channel> storage = new HashMap<>();

    public FileChannelRepository() {
        load();
    }

    @Override
    public Channel save(Channel channel) {
        storage.put(channel.getId(), channel);
        persist();
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return storage.get(id);
    }

    @Override
    public List<Channel> findAll() {
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
            throw new RuntimeException("Failed to persist channel data", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void load() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                storage = (Map<UUID, Channel>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                storage = new HashMap<>();
            }
        }
    }
}
