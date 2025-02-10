package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.interfacepac.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private static final String FILE_PATH = "tmp/user_status.ser";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<UUID, UserStatus> userStatusData;


    public FileUserStatusRepository() {
        this.userStatusData = loadFromFile();
    }

    @Override
    public void save(UserStatus userStatus) {
        userStatusData.put(userStatus.getId(), userStatus);
        saveToFile();
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatusData.values().stream()
                .filter(status -> userId.equals(status.getUser().getId()))
                .findFirst();
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusId) {
        return Optional.ofNullable(userStatusData.get(userStatusId));
    }

    @Override
    public boolean existsByUser(User user) {
        return userStatusData.values().stream()
                .anyMatch(status -> user.equals(status.getUser()));
    }

    @Override
    public boolean existsByUserStatusId(UUID userStatusId) {
        return userStatusData.containsKey(userStatusId);
    }

    @Override
    public boolean existByUserId(UUID userId) {
        return userStatusData.values().stream()
                .anyMatch(status -> userId.equals(status.getUser().getId()));
    }

    @Override
    public void delete(UserStatus userStatus) {
        userStatusData.remove(userStatus.getId());
        saveToFile();
    }

    @Override
    public void deleteById(UUID userStatusId) {
        userStatusData.remove(userStatusId);
        saveToFile();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        userStatusData.values()
                .removeIf(status -> userId.equals(status.getUser().getId()));
        saveToFile();
    }

    private Map<UUID, UserStatus> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<Map<UUID, UserStatus>>() {});
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(userStatusData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user status data to file.", e);
        }
    }




}
