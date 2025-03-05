package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;


@Repository
public class FileChannelRepository implements ChannelRepository {
    private final String filePath = "channels.dat";

    @Override
    public Channel save(Channel channel) {
        Map<String, Channel> channels = readFromFile();
        channels.put(channel.getId(), channel);
        writeToFile(channels);
        return channel;
    }

    @Override
    public void deleteById(String id) {
        Map<String, Channel> channels = readFromFile();
        channels.remove(id);
        writeToFile(channels);
    }

    @Override
    public Optional<Channel> findById(String id) {
        Map<String, Channel> channels = readFromFile();
        return Optional.ofNullable(channels.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(readFromFile().values());
    }

    private Map<String, Channel> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<String, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void writeToFile(Map<String, Channel> channels) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}