package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
//@Primary
public class FileReadStatusRepository implements ReadStatusRepository {
    private static final String FILE_PATH = "tmp/read_status.ser";
    private final FileManager<ReadStatus> fileManager =  new FileManager<>(FILE_PATH);

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Map<UUID, ReadStatus> savedReadStatusMap = loadReadStatusMapToFile();
        savedReadStatusMap.put(readStatus.getId(), readStatus);
        saveReadStatusMapToFile(savedReadStatusMap);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(loadReadStatusMapToFile().get(id));
    }

    @Override
    public List<ReadStatus> findAllUserId(UUID userId) {
        return fileManager.loadListToFile().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReadStatus> findAllChannelId(UUID channelId) {
        return fileManager.loadListToFile().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, ReadStatus> savedReadStatusMap = loadReadStatusMapToFile();
        savedReadStatusMap.remove(id);
        saveReadStatusMapToFile(savedReadStatusMap);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> savedReadStatusMap = loadReadStatusMapToFile();
        for (UUID key : savedReadStatusMap.keySet()) {
            if (savedReadStatusMap.get(key).getChannelId().equals(channelId)) {
                savedReadStatusMap.remove(key);
            }
        }
        saveReadStatusMapToFile(savedReadStatusMap);
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userid, UUID channelId) {
        return fileManager.loadListToFile().stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId)
                        && readStatus.getUserId().equals(userid));
    }

    private void saveReadStatusMapToFile(Map<UUID, ReadStatus> saveReadStatusMap) {
        List<ReadStatus> saveReadStatusList = saveReadStatusMap.values().stream().collect(Collectors.toList());
        fileManager.saveListToFile(saveReadStatusList);
    }

    private Map<UUID, ReadStatus> loadReadStatusMapToFile() {
        List<ReadStatus> loadReadStatusList = fileManager.loadListToFile();
        if (loadReadStatusList.isEmpty()) {
            return new HashMap<>();
        }
        return loadReadStatusList.stream()
                .collect(Collectors.toMap(ReadStatus::getId, Function.identity()));
    }
}
