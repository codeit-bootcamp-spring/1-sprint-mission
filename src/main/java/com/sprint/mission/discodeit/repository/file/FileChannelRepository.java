package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "channels.dat";

    @Override
    public void save(Channel channel) {
        Map<UUID, Channel> data = loadData();
        data.put(channel.getId(), channel);
        saveData(data);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Map<UUID, Channel> data = loadData();
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(loadData().values());
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> data = loadData();
        data.remove(id);
        saveData(data);
    }

    private Map<UUID, Channel> loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, Channel> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}