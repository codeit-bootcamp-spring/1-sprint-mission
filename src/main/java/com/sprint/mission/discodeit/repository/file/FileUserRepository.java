package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private final String FILE_NAME = "src/main/java/serialized/users.ser";
    private final Map<UUID, User> data;

    public FileUserRepository() {
        this.data = loadData();
    }

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        saveData();
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<User> findByName(String name) {
        return data.values().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean update(UUID id, String name, String email, UserStatus status) {
        boolean updated = data.computeIfPresent(id, (key, u) -> {
            u.update(name, email, status);
            return u;
        }) != null;
        if (updated) {
            saveData();
        }
        return updated;
    }

    @Override
    public boolean delete(UUID id) {
        boolean deleted = data.remove(id) != null;
        if (deleted) {
            saveData();
        }
        return deleted;
    }

    private Map<UUID, User> loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ConcurrentHashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}