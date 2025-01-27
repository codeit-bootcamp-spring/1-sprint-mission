package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.io.*;

public class FileUserService implements UserService {
    private final String FILE_PATH="user.ser";
    private Map<UUID, User> data;
    private static volatile FileUserService instance;

    public FileUserService() {
        this.data=loadDataFromFile();
    }

    //싱글톤
    public static FileUserService getInstance() {
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
    public void createUser(User user) {
        data.put(user.getId(),user);
        saveDataToFile();
    }


    @Override
    public Optional<User> getUserById(UUID id) {
        User userNullable=this.data.get(id);
        return Optional.ofNullable(Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not fount")));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, User updateUser) {
        User existingUser = data.get(id);
        if (existingUser != null) {
            existingUser.update(updateUser.getName(), updateUser.getEmail(), updateUser.getPassword());
            saveDataToFile();
        }
    }

    @Override
    public void deleteUser(UUID id) {
        if(!this.data.containsKey(id)){
            throw new NoSuchElementException("User with id " + id + " not fount");
        }
        data.remove(id);
        saveDataToFile();
    }

    // 데이터를 파일에 저장
    private void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file: " + e.getMessage(), e);
        }
    }

    // 파일에서 데이터를 로드
    private Map<UUID, User> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data from file: " + e.getMessage(), e);
        }
    }
}
