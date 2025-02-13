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
    private Map<UUID, Channel> store;

    @Autowired
    public FileChannelRepository(FileConfig fileConfig) {
        String fileDirectory = fileConfig.getFileDirectory();
        String fileName = fileConfig.getChannelJsonPath();
        this.channelJsonFile = fileDirectory + "/" + fileName;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // 처름에 로드
        store = loadChannelFromJson();
    }

    @Override
    public Channel save(Channel channel) {
        store.put(channel.getId(), channel);
        // 저장
        saveChannelToJson(store);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return store.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (store.remove(id) != null){
            saveChannelToJson(store);
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

    private void saveChannelToJson(Map<UUID, Channel> store) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(channelJsonFile), store);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
    }
}
