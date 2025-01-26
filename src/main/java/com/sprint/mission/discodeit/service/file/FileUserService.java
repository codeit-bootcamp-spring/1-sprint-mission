package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {
    private static volatile FileUserService instance;
    private final String FILE_NAME = "src/main/java/serialized/users.ser";
    private final Map<UUID, User> data;

    private FileUserService() {
        this.data = loadData();
    }

    protected static FileUserService getInstance() {
        if (instance == null) {
            synchronized (FileUserService.class) {
                if (instance == null) {
                    instance = new FileUserService();
                }
            }
        }
        return instance;
    }

    @Override
    public User register(User user) {
        data.put(user.getId(), user);
        saveData();
        return user;
    }

    @Override
    public Optional<User> getUserDetails(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean updateUserProfile(UUID id, String name, String email, UserStatus status) {
        boolean updated = data.computeIfPresent(id, (key, user) -> {
            user.update(name, email, status);
            return user;
        }) != null;
        if (updated) saveData();
        return updated;
    }

    @Override
    public boolean deleteUser(UUID id) {
        boolean deleted = (data.remove(id) != null);
        if (deleted) saveData();
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
