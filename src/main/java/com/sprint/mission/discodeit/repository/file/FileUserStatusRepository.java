package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.config.FileConfig;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
@Primary
public class FileUserStatusRepository implements UserStatusRepository {

    private final String userStatusJsonFile;
    private final ObjectMapper mapper;
    private Map<UUID, UserStatus> userStatusMap;

    @Autowired
    public FileUserStatusRepository(FileConfig fileConfig) {
        this.userStatusJsonFile = fileConfig.getUserStatusJsonPath();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        userStatusMap = new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatusMap.put(userStatus.getUserId(), userStatus);
        saveUserStatusesToJson(userStatusMap);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return Optional.ofNullable(userStatusMap.get(userId));
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusMap.values());
    }

    @Override
    public void deleteByUserId(UUID userId) {
        userStatusMap.remove(userId);
        saveUserStatusesToJson(userStatusMap);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return loadUserStatusesFromJson().containsKey(userId);
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

    private void saveUserStatusesToJson(Map<UUID, UserStatus> userStatusMap) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(userStatusJsonFile), userStatusMap);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
    }
}
