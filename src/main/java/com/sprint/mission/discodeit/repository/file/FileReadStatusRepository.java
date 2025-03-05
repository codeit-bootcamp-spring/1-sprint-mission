package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path directory;
    private final AtomicLong idGenerator = new AtomicLong(0);

    public FileReadStatusRepository(@Qualifier("fileReadStatusStoragePath") Path directory) {
        this.directory = directory;
    }

    @PostConstruct  // 의존성 주입이 완료된 후 실행
    private void init() {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                log.info("[ReadStatus] ReadStatus storage directory created: {}", directory.toAbsolutePath());
            } catch (IOException e) {
                throw new CustomException(ErrorCode.FAILED_TO_CREATE_DIRECTORY);
            }
        }
    }

    @Override
    public Long save(ReadStatus readStatus) {
        Long id = idGenerator.getAndIncrement();
        Path filePath = directory.resolve(id + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(readStatus);
            return id;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_TO_SAVE_DATA);
        }
    }

    @Override
    public ReadStatus load(Long id) {
        Path filePath = directory.resolve(id + ".ser");
        if (!Files.exists(filePath)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (ReadStatus) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new CustomException(ErrorCode.FAILED_TO_LOAD_DATA);
        }
    }

    @Override
    public Map.Entry<Long, ReadStatus> load(UUID uuid) {
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA));
    }

    @Override
    public Map<Long, ReadStatus> loadAll() {
        try {
            return Files.list(directory)
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            ReadStatus readStatus = (ReadStatus) ois.readObject();
                            Long id = Long.valueOf(path.getFileName().toString().replace(".ser", ""));
                            return Map.entry(id, readStatus);
                        } catch (IOException | ClassNotFoundException e) {
                            throw new CustomException(ErrorCode.FAILED_TO_LOAD_DATA);
                        }
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Files.deleteIfExists(directory.resolve(id + ".ser"));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_TO_DELETE_DATA);
        }
    }

    @Override
    public void update(Long id, ReadStatus readStatus) {
        Path filePath = directory.resolve(id + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(readStatus);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_TO_UPDATE_DATA);
        }
    }

    @Override
    public Map<Long, ReadStatus> findAllByUserId(UUID userId) {
        return loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getUserId().equals(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Instant findUpToDateReadTimeByChannelId(UUID uuid) {
        return loadAll().values().stream()
                .filter(entry -> entry.getChannelId().equals(uuid))
                .map(ReadStatus::getLastReadAt)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new CustomException(ErrorCode.FAILED_TO_LOAD_DATA));
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        loadAll().entrySet().stream()
                .filter(entry -> entry.getValue().getChannelId().equals(channelId))
                .map(Map.Entry::getKey)
                .forEach(this::delete);
    }
}
