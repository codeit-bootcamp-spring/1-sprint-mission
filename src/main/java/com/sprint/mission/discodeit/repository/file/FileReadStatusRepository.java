package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.config.FileConfig;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Primary
public class FileReadStatusRepository implements ReadStatusRepository {

    private final String readStatusJsonFile;
    private final ObjectMapper mapper;
    private Map<UUID, ReadStatus> readStatusMap;

    @Autowired
    public FileReadStatusRepository(FileConfig fileConfig) {
        this.readStatusJsonFile = fileConfig.getReadStatusJsonPath();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        readStatusMap = loadReadStatusesFromJson();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getId(), readStatus);
        saveReadStatusesToJson(readStatusMap);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findByUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public Optional<ReadStatus> findByChannelId(UUID channelId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(readStatusMap.get(id));
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusMap.remove(readStatusId);
        saveReadStatusesToJson(readStatusMap);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        readStatusMap.values().removeIf(rs -> rs.getChannelId().equals(channelId));
        saveReadStatusesToJson(readStatusMap);
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusMap.values().stream()
                .anyMatch(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId));
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

    private void saveReadStatusesToJson(Map<UUID, ReadStatus> readStatusMap) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(readStatusJsonFile), readStatusMap);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
    }
}
