package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.collection.Users;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {
    private final String filePath;
    private final Users users;

    public FileUserService(String filePath) {
        this.filePath = filePath;
        this.users = loadFromFile().orElseGet(Users::new);
    }

    @Override
    public User createUser(String username) {
        User newUser = new User(username);
        users.add(newUser.getId(), newUser);
        saveToFile();
        return newUser;
    }

    @Override
    public Map<UUID, User> getUsers() {
        return users.asReadOnly();
    }

    @Override
    public Optional<User> getUser(UUID id) {
        return users.get(id);
    }

    @Override
    public Optional<User> updateUser(UUID id, String newUsername) {
        Optional<User> updatedUser = users.update(id, newUsername);
        if (updatedUser.isPresent()) {
            saveToFile();
        }
        return updatedUser;
    }

    @Override
    public Optional<User> deleteUser(UUID id) {
        Optional<User> removedUser = users.remove(id);
        if (removedUser.isPresent()) {
            saveToFile();
        }
        return removedUser;
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("유저를 저장하는데 실패", e);
        }
    }

    private Optional<Users> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return Optional.of((Users) ois.readObject());
        } catch (FileNotFoundException e) {
            return Optional.empty();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("유저를 불러오는데 실패", e);
        }
    }
}
