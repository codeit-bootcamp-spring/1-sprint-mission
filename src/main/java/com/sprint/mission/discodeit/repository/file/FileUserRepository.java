package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.config.FileConfig;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {

    private final String userJsonFile;
    private final ObjectMapper mapper;
    private Map<UUID, User> userMap;

    @Autowired
    public FileUserRepository(FileConfig fileConfig) {
        String fileDirectory = fileConfig.getFileDirectory();
        String fileName = fileConfig.getUserJsonPath();
        this.userJsonFile = fileDirectory + "/" + fileName;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        userMap = new HashMap<>();
    }

    @Override
    public User save(User user) {
        userMap = loadUserFromJson();
        userMap.put(user.getUserId(), user);
        saveUsersToJson(userMap);
        return user;
    }

    @Override
    public Optional<User> findByUserId(UUID userId) {
        return Optional.ofNullable(loadUserFromJson().get(userId));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return loadUserFromJson().values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(loadUserFromJson().values());
    }

    @Override
    public void delete(UUID userId) {
        userMap = loadUserFromJson();
        if (userMap.containsKey(userId)) {
            userMap.remove(userId);
            saveUsersToJson(userMap);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return loadUserFromJson().values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return loadUserFromJson().values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    private Map<UUID, User> loadUserFromJson() {
        Map<UUID, User> map = new HashMap<>();
        File file = new File(userJsonFile);
        if (!file.exists()) {
            return map;
        }
        try {
            // JSON 파일 읽어 Map 형태로 변환
            map = mapper.readValue(file, new TypeReference<Map<UUID, User>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
        return map;
    }

    private void saveUsersToJson(Map<UUID, User> userMap) {
        try {
            // json 데이터를 보기좋게(pretty print) 정렬하여 저장
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(userJsonFile), userMap);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save read statuses to JSON file.", e);
        }
    }
}