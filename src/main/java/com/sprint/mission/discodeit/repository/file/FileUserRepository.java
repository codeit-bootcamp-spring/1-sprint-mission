package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private final String filePath = "users.dat";
    private Map<UUID, User> data = new HashMap<>();

    public FileUserRepository() {
        loadFromFile();
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
        saveToFile();
    }

    @Override
    public Optional<User> findById(UUID id) {
        loadFromFile();
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> findAll() {
        loadFromFile();
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

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Error saving data to file: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("데이터 파일이 없습니다. 새 파일을 생성합니다.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            data = (Map<UUID, User>) ois.readObject();
            System.out.println("데이터 로드 성공.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("파일을 읽는 중 오류 발생: " + e.getMessage());
        }
    }
}

