package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileUserRepository implements UserRepository {

    private final Path directory;

    public FileUserRepository() {
        this.directory = Paths.get("src", "main", "resources", "data", "serialized", "users");
    }

    @Override
    public User save(String name, String email, String phoneNumber, String password) {
        User user = new User(name, email, phoneNumber, password);
        Path filePath = directory.resolve(user.getId() + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            return users;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user, String name, String email, String phoneNumber) {
        user.update(name, email, phoneNumber);

        Path filePath = directory.resolve(user.getId() + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePassword(User user, String originalPassword, String newPassword) {
        user.updatePassword(originalPassword, newPassword);

        Path filePath = directory.resolve(user.getId() + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(User user) {
        Path filePath = directory.resolve(user.getId() + ".ser");

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            System.out.println("삭제에 실패하였습니다.");
        }
    }
}
