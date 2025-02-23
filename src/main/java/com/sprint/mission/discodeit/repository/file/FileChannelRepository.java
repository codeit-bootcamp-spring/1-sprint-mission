package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {
    private final String filePath = "channels.ser";
    private Map<UUID, Channel> data;


    public FileChannelRepository() {
        this.data = loadFromFile();
    }

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
        saveToFile();
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
    public void delete(UUID id) {
        data.remove(id);
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("채널을 저장하는데 실패하였습니다.", e);
        }
    }

    private Map<UUID, Channel> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널을 불러오는데 실패하였습니다", e);
        }
    }
}

