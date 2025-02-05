package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.validator.UserValidator;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private final UserValidator validator;
    private final Path directory;

    public FileUserService() {
        this.validator = new UserValidator();
        this.directory = Paths.get("src", "main", "resources", "data", "serialized", "users");
    }

    @Override
    public User create(String name, String email, String password) {
        validator.validate(name, email);
        User user = new User(name, email, password);
        Path filePath = directory.resolve(user.getId() + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 유저 생성에 실패하였습니다.");
        }
        return user;
    }

    @Override
    public User find(UUID userId) {
        Path filePath = directory.resolve(userId + ".ser");
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            Object data = ois.readObject();
            return (User) data;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("[ERROR] 유저를 찾을 수 없습니다.");
        }
    }

    @Override
    public List<User> findAll() {
        try {
            List<User> users = Files.list(directory)
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ) {
                            Object data = ois.readObject();
                            return (User) data;
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("[ERROR] 유저를 찾을 수 없습니다.");
                        }
                    })
                    .toList();
            return users;
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 유저를 찾을 수 없습니다.");
        }
    }

    @Override
    public String getInfo(UUID userId) {
        User user = find(userId);
        return user.toString();
    }

    @Override
    public void update(UUID userId, String name, String email) {
        validator.validate(name, email);
        User user = find(userId);
        user.update(name, email);

        Path filePath = directory.resolve(userId + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 유저 업데이트에 실패하였습니다.");
        }
    }

    @Override
    public void updatePassword(UUID userId, String originalPassword, String newPassword) {
        User user = find(userId);
        user.updatePassword(originalPassword, newPassword);

        Path filePath = directory.resolve(userId + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 유저 비밀번호 업데이트에 실패하였습니다.");
        }
    }

    @Override
    public void delete(UUID userId) {
        Path filePath = directory.resolve(userId + ".ser");

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            System.out.println("[ERROR] 유저 삭제에 실패하였습니다.");
        }
    }

    @Override
    public void isDuplicateEmail(String email) {
        List<User> users = findAll();
        users.forEach(user -> user.isDuplicateEmail(email));
    }
}
