package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileChannelRepository implements ChannelRepository {

    private static final String FILE_PATH = "tmp/entity/channel.ser";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<User, List<Channel>> channelData;



    public FileChannelRepository() {
        this.channelData = loadFromFile();
    }

    @Override
    public Channel save(Channel channel) {
        List<Channel> channels = new ArrayList<>();
        channels.add(channel);
        channelData.put(channel.getOwner(), channels);
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
    public List<Channel> findAllByType(ChannelType type) {
        return channelData.values().stream()
                .flatMap(List::stream)
                .filter(channel -> channel.getType() ==type)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByChannel(Channel channel) {
        List<Channel> channels = channelData.get(channel.getOwner());
        if(channels != null) {
            channels.remove(channel);
            if(channels.isEmpty()) {
                channelData.remove(channel.getOwner());
            }
            saveToFile();
        }
    }


    @Override
    public boolean existsByUser(User user) {
        return channelData.values().stream()
                .flatMap(List::stream)
                .anyMatch(channel -> channel.getOwner().equals(user));
    }


    @Override
    public List<Channel> findAllByOwnerAndType(User owner, ChannelType type) {
        return channelData.getOrDefault(owner, new ArrayList<>())
                .stream()
                .filter(channel -> channel.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return channelData.values().stream()
                .flatMap(List::stream)
                .filter(channel -> channel.getOwner().getId().equals(userId) ||
                        (channel.getType() == ChannelType.PRIVATE))
                .collect(Collectors.toList());
    }

    private Map<User, List<Channel>> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<Map<User, List<Channel>>>(){});
        } catch (IOException e) {
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
