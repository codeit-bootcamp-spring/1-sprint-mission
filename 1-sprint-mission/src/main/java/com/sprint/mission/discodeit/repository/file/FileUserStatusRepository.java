package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.interfacepac.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class FileUserStatusRepository implements UserStatusRepository {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String filePath;
    private final Map<UUID, UserStatus> userStatusData;

    public FileUserStatusRepository(@Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        if(!fileDirectory.endsWith("/")) {
            fileDirectory += "/";
        }
        this.filePath = fileDirectory + "user_status.json";
        ensureDirectoryExists(this.filePath);
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

    private void ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        File parentDirectory = directory.getParentFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
    }

    private Map<UUID, UserStatus> loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<Map<UUID, UserStatus>>() {});
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }

    private void saveToFile() {
        File file = new File(filePath);
        try {
            objectMapper.writeValue(file, userStatusData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user status data to file.", e);
        }
    }


}
