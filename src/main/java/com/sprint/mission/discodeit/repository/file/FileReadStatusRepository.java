package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.config.FileConfig;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    private final String readStatusJsonFile;
    private final ObjectMapper mapper;
    private Map<UUID, ReadStatus> store;

    @Autowired
    public FileReadStatusRepository(FileConfig fileConfig) {
        String fileDirectory = fileConfig.getFileDirectory();
        String fileName = fileConfig.getReadStatusJsonPath();
        this.readStatusJsonFile = fileDirectory + "/" + fileName;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        store = loadReadStatusesFromJson();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        store.put(readStatus.getId(), readStatus);
        saveReadStatusesToJson(store);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return store.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return store.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .toList();
    }
    @Override
    public boolean existsById(UUID id) {
        return store.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (store.remove(id) != null){
            saveReadStatusesToJson(store);
        }
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        store.values().removeIf(rs -> rs.getChannelId().equals(channelId));
        saveReadStatusesToJson(store);
    }

    private Map<UUID, ReadStatus> loadReadStatusesFromJson() {
        Map<UUID, ReadStatus> map = new HashMap<>();
        File file = new File(readStatusJsonFile);
        if (!file.exists()) {
            return map;
        }
        try {
            map = mapper.readValue(file, new TypeReference<Map<UUID, ReadStatus>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
        return map;
    }

    private void saveReadStatusesToJson(Map<UUID, ReadStatus> store) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(readStatusJsonFile), store);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
    }
}
