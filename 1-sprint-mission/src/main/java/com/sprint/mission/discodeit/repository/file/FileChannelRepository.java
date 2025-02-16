package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;


import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
@Slf4j
public class FileChannelRepository implements ChannelRepository {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String filePath;
    private final Map<UUID, List<Channel>> channelData;

    public FileChannelRepository(@Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        if(!fileDirectory.endsWith("/")) {
            fileDirectory += "/";
        }
        this.filePath = fileDirectory + "channel.json";
        log.info("***** FileChannelRepository CONSTRUCTOR CALLED *****"); // 활성화 됐는지 확인
        ensureDirectoryExists(this.filePath);
        this.channelData = loadFromFile();
    }

    @Override
    public Channel save(Channel channel) {
        UUID ownerId = channel.getOwner().getId();
        List<Channel> channels = channelData.computeIfAbsent(ownerId, k -> new ArrayList<>());
        channels.add(channel);
        saveToFile();
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return channelData.values().stream()
                .flatMap(List::stream)
                .filter(channel -> channel.getId().equals(id))
                .findFirst();
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
                .filter(channel -> channel.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByChannel(Channel channel) {
        UUID ownerId = channel.getOwner().getId();
        List<Channel> channels = channelData.get(ownerId);
        if (channels != null) {
            channels.remove(channel);
            if (channels.isEmpty()) {
                channelData.remove(ownerId);
            }
            saveToFile();
        }
    }

    @Override
    public boolean existsByUser(User user) {
        return channelData.containsKey(user.getId());
    }

    @Override
    public List<Channel> findAllByOwnerAndType(User owner, ChannelType type) {
        UUID ownerId = owner.getId();
        return channelData.getOrDefault(ownerId, new ArrayList<>())
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

    //


    private void ensureDirectoryExists(String fileDirectory) {
        File directory = new File(fileDirectory);
        File parentDirectory = directory.getParentFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
    }

    private Map<UUID, List<Channel>> loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try {
            return objectMapper.readValue(
                    file,
                    new TypeReference<Map<UUID, List<Channel>>> () {}
            );
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }

    private void saveToFile() {
        File file = new File(filePath);
        try {
            objectMapper.writeValue(file, channelData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save channel to file.", e);
        }
    }


}
