package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.*;



public class FileUserService implements UserService {
    private static final String FILE_PATH = "files/users.ser";
    private List<User> users;

    public FileUserService() {
        ensureDirectoryExists("files");
        if (users == null || users.isEmpty()) { // 중복 방지
            this.users = loadFromFile();
        }
    }

    //++++++++++
    @Override
    public User createUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("이미 존재하는 유저입니다: " + username);
                return user; // 중복 유저 반환
            }
        }
        User user = new User(username, password);
        users.add(user);
        saveToFile();
        return user;
    }

    @Override
    public User getUserById(UUID userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public User updateUsername(UUID userId, String newUsername) {
        User user = getUserById(userId);
        if (user != null){
            user.updateUsername(newUsername);
            saveToFile();
        }
        return user;
    }

    @Override
    public User updatePassword(UUID userId, String newPassword) {
        User user = getUserById(userId);
        if (user != null){
            user.updatePassword(newPassword);
            saveToFile();
        }
        return user;
    }

    @Override
    public boolean deleteUser(UUID userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userId)){
                users.remove(i);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //++++ 테스트
    @SuppressWarnings("unchecked")
    private List<User> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("디렉토리가 생성되었습니다: " + directory.getAbsolutePath());
            } else {
                System.out.println("디렉토리 생성에 실패했습니다.");
            }
        }
    }

}
