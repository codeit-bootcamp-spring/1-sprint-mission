package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.config.FileConfig;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {

    private final String userStatusJsonFile;
    private final ObjectMapper mapper;
    private Map<UUID, UserStatus> store;

    @Autowired
    public FileUserStatusRepository(FileConfig fileConfig) {
        String fileDirectory = fileConfig.getFileDirectory();
        String fileName = fileConfig.getUserStatusJsonPath();
        this.userStatusJsonFile = fileDirectory + "/" + fileName;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        store = loadUserStatusesFromJson();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        store.put(userStatus.getId(), userStatus);
        saveUserStatusesToJson(store);
        return userStatus;
    }
    
    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return store.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return store.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        if (store.remove(id) != null){
            saveUserStatusesToJson(store);
        }
    }

    @Override
    public void deleteByUserId(UUID userId) {
        store.values().removeIf(userStatus -> userStatus.getUserId().equals(userId));
        saveUserStatusesToJson(store);
    }

    private Map<UUID, UserStatus> loadUserStatusesFromJson() {
        Map<UUID, UserStatus> map = new HashMap<>();
        File file = new File(userStatusJsonFile);
        if (!file.exists()) {
            return map;
        }
        try {
            map = mapper.readValue(file, new TypeReference<Map<UUID, UserStatus>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
        return map;
    }

    private void saveUserStatusesToJson(Map<UUID, UserStatus> store) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(userStatusJsonFile), store);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
    }
}
