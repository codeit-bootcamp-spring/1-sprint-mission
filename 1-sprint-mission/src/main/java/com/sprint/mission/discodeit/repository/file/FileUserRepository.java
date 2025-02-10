package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "tmp/entity/users.ser";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<UUID, User> userdata;

    public FileUserRepository() {
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
        return Optional.ofNullable(userdata.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found User")));
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
    public boolean existsByPassword(String password) {
        return userdata.values().stream().anyMatch(user -> user.getPassword().equals(password));
    }

    @Override
    public boolean existsById(UUID id) {
        return userdata.containsKey(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userdata.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userdata.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not User Found")));
    }

    private Map<UUID, User> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(file, new TypeReference<Map<UUID, User>>() {});
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new HashMap<>();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos
                     = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(userdata);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save users to file.", e);
        }

    }



}
