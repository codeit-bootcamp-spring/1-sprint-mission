package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();
    private final String filePath = "channels.dat";

    public FileChannelService() {
        loadFromFile();
    }

    @Override
    public void create(Channel channel) {
        data.put(channel.getId(), channel);
        saveToFile();
    }

    @Override
    public Optional<Channel> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> readAll() {
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

    // 데이터를 파일에 저장
    private void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일에서 데이터를 불러오기
    private void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            data.putAll((Map<UUID, Channel>) in.readObject());
        } catch (FileNotFoundException e) {
            System.out.println("Channel data file not found, creating a new one.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
