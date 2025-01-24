package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private static final String FILE_PATH = "users.dat";

    @Override
    public void createUser(User user) {
        List<User> users = getAllUsers();
        users.add(user);
        saveUsers(users);
    }

    @Override
    public User getUser(UUID id) {
        List<User> users = getAllUsers();
        for (User user : users) {
            if(user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("파일에서 유저를 읽지 못했습니다.", e);
        }
    }

    @Override
    public void updateUser(UUID id, String userName) {
        List<User> users = getAllUsers();
        User targetUser = users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저를 찾을 수 없습니다: " + id));

        targetUser.update(userName); // update 메서드로 updatedAt 갱신 포함
        saveUsers(users);
    }

    @Override
    public void deleteUser(UUID id) {
        List<User> users = getAllUsers();
        users.removeIf(user -> user.getId().equals(id));
        saveUsers(users);
    }

    private void saveUsers(List<User> users) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new IllegalStateException("유저를 파일에 저장하지 못했습니다.", e);
        }
    }

}
