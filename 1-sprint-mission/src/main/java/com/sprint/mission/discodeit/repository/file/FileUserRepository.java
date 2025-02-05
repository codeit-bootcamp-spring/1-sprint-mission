package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "tmp/users.ser";
    private final Map<UUID, User> userdata;

    public FileUserRepository() {
        this.userdata = loadFromFile();
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

    private Map<UUID, User> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
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
