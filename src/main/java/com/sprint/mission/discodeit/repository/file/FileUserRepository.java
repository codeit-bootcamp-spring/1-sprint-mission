package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;
public class FileUserRepository implements UserRepository {
    private final String filePath = "users.ser";
    private Map<UUID, User> data;

    public FileUserRepository() {
        this.data = loadFromFile();
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
        saveToFile();
    }

    @Override
    public User findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("유저를 저장하는데 실패하였습니다.", e);
        }
    }

    private Map<UUID, User> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("유저를 불러오는데 실패하였습니다", e);
        }
    }
}

