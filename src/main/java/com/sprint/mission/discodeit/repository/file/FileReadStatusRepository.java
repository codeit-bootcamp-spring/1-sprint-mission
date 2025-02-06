package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Primary
public class FileReadStatusRepository implements ReadStatusRepository {

    private static final String READ_STATUS_JSON_FILE = "tmp/readStatuses.json";
    private final ObjectMapper mapper;
    private Map<UUID, ReadStatus> readStatusMap;

    public FileReadStatusRepository() {
        mapper = new ObjectMapper();
        readStatusMap = loadReadStatusesFromJson();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getId(), readStatus);
        saveReadStatusesToJson(readStatusMap);
        return readStatus;
    }

    @Override
    public ReadStatus findByUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ReadStatus findByChannelId(UUID channelId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusMap.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public ReadStatus findById(UUID id) {
        return readStatusMap.get(id);
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
        File file = new File(READ_STATUS_JSON_FILE);
        if (!file.exists()) {
            return map;
        }
        try {
            map = mapper.readValue(file, new TypeReference<Map<UUID, ReadStatus>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void saveReadStatusesToJson(Map<UUID, ReadStatus> readStatusMap) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(READ_STATUS_JSON_FILE), readStatusMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
