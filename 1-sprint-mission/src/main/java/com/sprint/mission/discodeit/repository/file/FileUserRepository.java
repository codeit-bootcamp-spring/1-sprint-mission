package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class FileUserRepository implements UserRepository {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String filePath;
    private final Map<UUID, User> userdata;

    public FileUserRepository(
            @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        if(!fileDirectory.endsWith("/")) {
            fileDirectory += "/";
        }
        this.filePath = fileDirectory + "users.json";
        ensureDirectoryExists(this.filePath);
        this.userdata = loadFromFile();
    }



    @Override
    public boolean existsByUsername(String username) {
        return userdata.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public User save(User user) {
        userdata.put(user.getId(), user);
        saveToFile();
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(userdata.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userdata.values());
    }

    @Override
    public void deleteById(UUID id) {
        userdata.remove(id);
        saveToFile();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userdata.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userdata.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }



    private Map<UUID, User> loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<Map<UUID, User>>() {});
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }

    private void saveToFile() {
        File file = new File(filePath);
        try{
            objectMapper.writeValue(file, userdata);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save user to file.", e);
        }
    }

    private void ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        File parentDirectory = directory.getParentFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
    }
}

