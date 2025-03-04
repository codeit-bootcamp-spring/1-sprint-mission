package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@Repository
@ConditionalOnProperty(name = "sprint-mission.repository.type", havingValue = "file")
public class FileReadStatusRepository implements ReadStatusRepository {
    private static final String FILE_PATH = "files/read_statuses.ser";
    private Map<UUID, ReadStatus> readStatuses;

    public FileReadStatusRepository() {
        this.readStatuses = loadFromFile();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatuses.put(readStatus.getId(), readStatus);
        saveToFile();
        return readStatus;
    }

    @Override
    public ReadStatus findById(UUID id) {
        return readStatuses.get(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(readStatuses.values());
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        List<ReadStatus> result = new ArrayList<>();
        for (ReadStatus status : readStatuses.values()) {
            if (status.getUserId().equals(userId)) {
                result.add(status);
            }
        }
        return result;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        List<ReadStatus> result = new ArrayList<>();
        for (ReadStatus status : readStatuses.values()) {
            if (status.getChannelId().equals(channelId)) {
                result.add(status);
            }
        }
        return result;
    }

    @Override
    public boolean existsById(UUID id) {
        return readStatuses.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        readStatuses.remove(id);
        saveToFile();
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        List<UUID> toDelete = new ArrayList<>();
        for (Map.Entry<UUID, ReadStatus> entry : readStatuses.entrySet()) {
            if (entry.getValue().getChannelId().equals(channelId)) {
                toDelete.add(entry.getKey());
            }
        }
        for (UUID id : toDelete) {
            readStatuses.remove(id);
        }
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(readStatuses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<UUID, ReadStatus> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, ReadStatus>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}