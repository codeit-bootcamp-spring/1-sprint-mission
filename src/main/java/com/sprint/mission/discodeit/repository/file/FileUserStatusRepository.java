package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.io.*;
import java.util.*;

//@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final String FILE_PATH="userstatus.ser";
    private final Map<UUID, UserStatus> data;

    public FileUserStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(UserStatus status) {
        data.put(status.getId(), status);
        saveDataToFile();
    }

    @Override
    public void deleteByUserId(UUID id) {
        data.values().removeIf(user -> user.getId().equals(id));
        saveDataToFile();
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return data.values().stream().filter(userStatus -> userStatus.getId().equals(userId)).findFirst();
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
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
    private Map<UUID, UserStatus> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, UserStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data from file: " + e.getMessage(), e);
        }
    }
}
