package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileChannelRepository implements ChannelRepository {

    private static final String FILE_PATH = "tmp/channel.ser";
    private final Map<User, List<Channel>> channelData;



    public FileChannelRepository() {
        this.channelData = loadFromFile();
    }

    @Override
    public Channel save(Channel channel) {
        List<Channel> channels = new ArrayList<>();
        channels.add(channel);
        channelData.put(channel.getUser(), channels);
        saveToFile();
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channelData.values().stream()
                .flatMap(List::stream)
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found Channel ")));
    }

    @Override
    public List<Channel> findAll() {
        return channelData.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteByChannel(Channel channel) {
        List<Channel> channels = channelData.get(channel.getUser());
        if(channels != null) {
            channels.remove(channel);
            if(channels.isEmpty()) {
                channelData.remove(channel.getUser());
            }
            saveToFile();
        }
    }


    @Override
    public boolean existsByUser(User user) {
        return channelData.values().stream()
                .flatMap(List::stream)
                .anyMatch(channel -> channel.getUser().equals(user));
    }

    private Map<User, List<Channel>> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (Map<User, List<Channel>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return new HashMap<>();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos
                     = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channelData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save channel to file.", e);
        }

    }


}
