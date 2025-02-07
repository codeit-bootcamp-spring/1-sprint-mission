package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

// @Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final String FILE_PATH="binary.ser";
    private final Map<UUID, BinaryContent> data;

    public FileBinaryContentRepository() {
        this.data = loadDataFromFile();
    }

    @Override
    public void save(BinaryContent newProfile) {
        data.put(newProfile.getId(), newProfile);
        saveDataToFile();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        data.values().removeIf(user -> user.getUserId().equals(userId));
        saveDataToFile();
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> result = new ArrayList<>();
        for (UUID id : ids) {
            BinaryContent binaryContent = data.get(id);
            if (binaryContent != null) {
                result.add(binaryContent);
            }
        }
        return result;
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        saveDataToFile();
    }

    @Override
    public void deleteByMessageId(UUID id) {
        data.values().removeIf(message -> message.getMessageId().equals(id));
        saveDataToFile();
    }

    // 데이터를 파일에 저장
    private void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file: " + e.getMessage(), e);
        }
    }

    // 파일에서 데이터를 로드
    private Map<UUID, BinaryContent> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, BinaryContent>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data from file: " + e.getMessage(), e);
        }
    }
}
