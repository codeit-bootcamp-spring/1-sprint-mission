package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private final Map<UUID, User> data = new HashMap<>();
    private final String filePath = "users.dat";

    public FileUserService() {
        loadFromFile();
    }

    @Override
    public void create(User user) {
        data.put(user.getId(), user);
        saveToFile();
    }

    @Override
    public Optional<User> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, User user) {
        if (data.containsKey(id)) {
            data.put(id, user);
            saveToFile();
        } else {
            throw new IllegalArgumentException("User not found: " + id);
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
        saveToFile();
    }

    // 데이터를 파일에 저장
    private void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일에서 데이터를 로드
    private void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            data.putAll((Map<UUID, User>) in.readObject());
        } catch (FileNotFoundException e) {
            System.out.println("User data file not found, creating a new one.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
