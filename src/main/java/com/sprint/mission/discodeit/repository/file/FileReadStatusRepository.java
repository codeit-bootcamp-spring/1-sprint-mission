package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

//@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final String FILE_PATH="readStatus.ser";
    private final Map<UUID, ReadStatus> data;

    public FileReadStatusRepository() {
        this.data = loadDataFromFile();
    }

    @Override
    public void saveReadStatus(List<ReadStatus> readStatuses) {
        for (ReadStatus readStatus : readStatuses) {
            data.put(UUID.randomUUID(), readStatus);
            saveDataToFile();
        }
    }

    @Override
    public List<UUID> findMembersByChannelId(UUID id) {
        List<UUID> members = new ArrayList<>();
        for (ReadStatus readStatus : data.values()) {
            if (readStatus.getChannelId().equals(id)) {
                members.add(readStatus.getUserId());
            }
        }
        return members;
    }

    @Override
    public void deleteByChannelId(UUID id) {
        data.values().removeIf(readStatus -> readStatus.getChannelId().equals(id));
        saveDataToFile();
    }

    @Override
    public boolean isUserMemberOfChannel(UUID userId, UUID channelId) {
        return data.values().stream()
                .anyMatch(readStatus -> readStatus.getUserId().equals(userId) && readStatus.getChannelId().equals(channelId));
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        List<ReadStatus> result = new ArrayList<>();
        for (ReadStatus readStatus : data.values()) {
            if (readStatus.getUserId().equals(userId)) {
                result.add(readStatus);
            }
        }
        return result;
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        saveDataToFile();
    }

    // 데이터를 파일에 저장
    private void saveDataToFile() {
        File file = new File(FILE_PATH);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file: " + e.getMessage(), e);
        }
    }

    // 파일에서 데이터를 로드
    private Map<UUID, ReadStatus> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, ReadStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data from file: " + e.getMessage(), e);
        }
    }
}
