package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static final Path filePath = Paths.get(System.getProperty("user.dir"), "tmp/channel.ser");

    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> savedChannelMap = loadChannelMapToFile();
        savedChannelMap.put(channel.getId(), channel);
        saveChannelMapToFile(savedChannelMap);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        Map<UUID, Channel> savedChannelMap = loadChannelMapToFile();
        return Optional.ofNullable(savedChannelMap.get(channelId));
    }

    @Override
    public List<Channel> findAll() {
        return loadChannelMapToFile().values().stream().toList();
    }

    @Override
    public void delete(UUID channelId) {
        Map<UUID, Channel> savedChannelMap = loadChannelMapToFile();
        savedChannelMap.remove(channelId);
        saveChannelMapToFile(savedChannelMap);
    }

    private void saveChannelMapToFile(Map<UUID, Channel> saveChannelMap) {
        List<Channel> saveChannelList = saveChannelMap.values().stream().toList();
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile(),false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            for (Channel channel : saveChannelList) {
                oos.writeObject(channel);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<UUID, Channel> loadChannelMapToFile() {
        Map<UUID, Channel> data = new HashMap<>();
        if(!Files.exists(filePath)) {
            return data;
        }
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                Channel serChannel = (Channel) ois.readObject();
                data.put(serChannel.getId(), serChannel);
            }
        } catch (EOFException e) {
//            System.out.println("read all objects");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
