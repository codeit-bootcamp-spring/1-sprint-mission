package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "users.dat";
    private List<User> cachedUsers;

    public FileUserRepository() {
        this.cachedUsers = loadUsersFromFile();
    }

    @Override
    public void createUser(User user) {
        if (cachedUsers.stream().anyMatch(u -> u.getId().equals(user.getId()))) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다: " + user.getId());
        }
        cachedUsers.add(user);
        saveUsersToFile();
    }

    @Override
    public Optional<User> getUser(UUID id) {
        return cachedUsers.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(cachedUsers);
    }

    @Override
    public void updateUser(UUID id, String userName) {
        User user = getUser(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저를 찾을 수 없습니다: " + id));
        user.update(userName);
        saveUsersToFile();
    }

    @Override
    public void deleteUser(UUID id) {
        cachedUsers.removeIf(user -> user.getId().equals(id));
        saveUsersToFile();
    }

    private List<User> loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object readObject = ois.readObject();
            if (readObject instanceof List<?>) {
                return (List<User>) readObject;
            }
            throw new IllegalStateException("파일 데이터가 올바른 형식이 아닙니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("파일에서 유저를 읽지 못했습니다.", e);
        }
    }

    private void saveUsersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(cachedUsers);
        } catch (IOException e) {
            throw new IllegalStateException("유저를 파일에 저장하지 못했습니다.", e);
        }
    }
}
