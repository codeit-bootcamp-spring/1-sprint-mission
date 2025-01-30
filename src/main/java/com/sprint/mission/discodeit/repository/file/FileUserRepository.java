package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.entity.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "src/main/resources/users.ser";
    private Map<String, User> userData;

    public FileUserRepository() {
        this.userData = loadData();
    }

    @Override
    public User saveUser(User user) {
        userData.put(user.getEmail(), user);
        saveData();
        return user;
    }

    @Override
    public void deleteUser(User user) {
        userData.remove(user.getEmail());
        saveData();
    }

    @Override
    public User findById(String email) {
        return userData.get(email);
    }

    @Override
    public List<User> printAllUser() {
        return new ArrayList<>(userData.values());
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(userData);
        } catch (IOException e) {
            System.err.println("===데이터 저장 중 오류 발생: " + e.getMessage() + "===");
        }
    }


    private Map<String, User> loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("===데이터 로드 중 오류 발생: " + e.getMessage() + "===");
            return new HashMap<>();
        }
    }
}