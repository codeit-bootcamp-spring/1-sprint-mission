package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository {
    private final String filePath;

    public FileReadStatusRepository(@Value("${file.path.readstatus}") String filePath) {
        this.filePath = filePath;
    }
    @Override
    public ReadStatus save(ReadStatus readStatus) {
        List<ReadStatus> statuses = load();
        statuses.add(readStatus);
        saveToFile(statuses);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return load().stream()
                .filter(status -> status.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return load().stream()
                .filter(status -> status.getOwnerId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return load().stream()
                .filter(status -> status.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return load().stream()
                .anyMatch(status -> status.getId().equals(id));
    }

    @Override
    public void deleteById(UUID id) {
        List<ReadStatus> statuses = load().stream()
                .filter(status -> !status.getId().equals(id)) // 해당 ID 제외하고 저장
                .toList();
        saveToFile(statuses);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        List<ReadStatus> statuses = load().stream()
                .filter(status -> !status.getChannelId().equals(channelId)) // 해당 채널 제외하고 저장
                .toList();
        saveToFile(statuses);
    }

    private List<ReadStatus> load() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<ReadStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 읽기 실패: " + file.getAbsolutePath(), e);
        }
    }

    private void saveToFile(List<ReadStatus> statuses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(statuses);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + filePath, e);
        }
    }
}
