package com.srint.mission.discodeit.service.file;

import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {

    private static final String FILE_PATH = "userData.ser";
    private Map<UUID, User> data;

    public FileUserService() {
        this.data = loadDataFromFile();
    }

    // 데이터 파일 읽기
    @SuppressWarnings("unchecked")
    private Map<UUID, User> loadDataFromFile() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new HashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // 데이터 파일에 쓰기
    private void saveDataToFile() {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UUID save(User user) {
        data.put(user.getId(), user);
        saveDataToFile();
        return user.getId();
    }

    @Override
    public User findOne(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("조회할 User를 찾지 못했습니다.");
        }
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("User가 없습니다.");
        }
        return new ArrayList<>(data.values());
    }

    @Override
    public UUID delete(UUID id) {
        if (!data.containsKey(id)) {
            throw new IllegalArgumentException("삭제할 User를 찾지 못했습니다.");
        }
        data.remove(id);
        saveDataToFile();
        return id;
    }



    @Override
    public UUID create(String username, String email, String password) {
        validateDuplicateUser(email);
        User user = new User(username, email, password);
        return save(user);
    }

    @Override
    public User read(UUID id) {
        return findOne(id);
    }

    @Override
    public List<User> readAll() {
        return findAll();
    }

    @Override
    public User updateUserName(UUID id, String name) {
        User findUser = findOne(id);
        findUser.setUsername(name);
        saveDataToFile();
        return findUser;
    }

    @Override
    public User updateEmail(UUID id, String email) {
        User findUser = findOne(id);
        findUser.setEmail(email);
        saveDataToFile();
        return findUser;
    }

    @Override
    public User updatePassword(UUID id, String password) {
        User findUser = findOne(id);
        findUser.setPassword(password);
        saveDataToFile();
        return findUser;
    }

    @Override
    public UUID deleteUser(UUID id) {
        User findUser = findOne(id);
        if (!findUser.getMyChannels().isEmpty()) {
            findUser.getMyChannels().forEach(
                    channel -> channel.deleteJoinedUser(findUser));
        }
        UUID deletedId = delete(findUser.getId());
        saveDataToFile();
        return deletedId;
    }

    private void validateDuplicateUser(String email) {
        if (!data.isEmpty()) {
            List<User> users = findAll();
            if (users.stream().anyMatch(user -> email.equals(user.getEmail()))) {
                throw new IllegalStateException("이미 존재하는 이메일입니다.");
            }
        }
    }
}
