package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.config.FileConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {

    private final String channelJsonFile;
    private final ObjectMapper mapper;
    private Map<UUID, Channel> channelMap;

    @Autowired
    public FileChannelRepository(FileConfig fileConfig) {
        String fileDirectory = fileConfig.getFileDirectory();
        String fileName = fileConfig.getChannelJsonPath();
        this.channelJsonFile = fileDirectory + "/" + fileName;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        channelMap = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        channelMap = loadChannelFromJson();
        channelMap.put(channel.getId(), channel);
        saveChannelToJson(channelMap);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(loadChannelFromJson().get(channelId));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(loadChannelFromJson().values());
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        channelMap = loadChannelFromJson();
        if (channelMap.containsKey(channelId)) {
            channelMap.remove(channelId);
            saveChannelToJson(channelMap);
        }
    }

    private Map<UUID, Channel> loadChannelFromJson() {
        Map<UUID, Channel> map = new HashMap<>();
        File file = new File(channelJsonFile);
        if (!file.exists()) {
            return map;
        }
        try {
            map = mapper.readValue(file, new TypeReference<Map<UUID, Channel>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
        return map;
    }

    private void saveChannelToJson(Map<UUID, Channel> channelMap) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(channelJsonFile), channelMap);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
    }
}
