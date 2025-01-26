package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserRepository implements UserRepository {
    private final String filePath = "users.ser";
    private Map<UUID, User> storage = new HashMap<>();

    public FileUserRepository() {
        load();
    }

    @Override
    public User save(User user) {
        storage.put(user.getId(), user);
        persist();
        return user;
    }

    @Override
    public User findById(UUID id) {
        return storage.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
        persist();
    }

    // 직렬화하여 파일에 저장
    private void persist() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(storage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist user data", e);
        }
    }

    // 파일에서 읽어와 역직렬화
    @SuppressWarnings("unchecked")
    private void load() {
        File file = new File(filePath);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                storage = (Map<UUID, User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                storage = new HashMap<>();
            }
        }
    }
}
