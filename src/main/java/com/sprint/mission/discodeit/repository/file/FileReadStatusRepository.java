package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.sprint.mission.discodeit.util.FileIOUtil.loadFromFile;
import static com.sprint.mission.discodeit.util.FileIOUtil.saveToFile;

@Profile("file")
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path filePath;
    private final Map<UUID, ReadStatus> readStatusMap;

    public FileReadStatusRepository(@Value("${discodeit.repository.read-status-file-path}") Path filePath) {
        this.filePath = filePath;
        if (!Files.exists(this.filePath)) {
            try {
                Files.createFile(this.filePath);
                saveToFile(new HashMap<>(), this.filePath);
            } catch (IOException e) {
                throw new RuntimeException("회원 파일을 초기화 하던 중에 문제가 발생했습니다", e);
            }
        }
        this.readStatusMap = loadFromFile(this.filePath);
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getId(), readStatus);
        saveToFile(readStatusMap, filePath);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<ReadStatus> findByChannelId(UUID channelID) {
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelID))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userID) {
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userID))
                .toList();
    }

    @Override
    public void delete(ReadStatus readStatus) {
        readStatusMap.remove(readStatus.getId());
        saveToFile(readStatusMap, filePath);
    }
}
