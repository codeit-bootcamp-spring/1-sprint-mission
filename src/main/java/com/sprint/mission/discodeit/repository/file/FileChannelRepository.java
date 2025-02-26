package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "sprint-mission.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "files/channels.ser";
    private Map<UUID, Channel> channels;

    public FileChannelRepository() {
        this.channels = loadFromFile();
    }

    @Override
    public Channel save(Channel channel) {
        channels.put(channel.getId(), channel);
        saveToFile();
        return channel;
    }

    @Override
    public Channel findByChannelname(String channelname) {
        for (Channel channel : channels.values()) {
            if (channel.getChannelName().equals(channelname)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public Channel findById(UUID id) {
        return channels.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return channels.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        channels.remove(id);
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<UUID, Channel> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}