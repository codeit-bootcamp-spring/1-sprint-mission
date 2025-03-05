package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.collection.Users;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {
    private final String filePath;

    public FileUserRepository(@Value("${file.path.user}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public synchronized User save(User newUser) {
        Users users = load().orElse(new Users());
        users.add(newUser.getId(), newUser);
        saveToFile(users);
        return newUser;
    }

    @Override
    public List<User> getAllUsers() {
        return load().map(Users::getUsersList).orElse(List.of());
    }

    @Override
    public User getUserById(UUID id) {
        return load()
                .flatMap(users -> users.get(id))
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자를 찾을 수 없습니다."));
    }

    @Override
    public synchronized void deleteById(UUID id) {
        Users users = load().orElse(new Users());
        users.remove(id);
        saveToFile(users);
    }

    @Override
    public void save() {
    }

    @Override
    public boolean existsByUsername(String username) {
        return load().map(users ->
                users.getUsersList().stream().anyMatch(user -> user.getUsername().equals(username))
        ).orElse(false);
    }

    @Override
    public boolean existsByEmail(String email) {
        return load().map(users ->
                users.getUsersList().stream().anyMatch(user -> user.getEmail().equals(email))
        ).orElse(false);
    }

    @Override
    public Optional<User> getUserByEmail(String userEmail) {
        return load().flatMap(users ->
                users.getUsersList().stream()
                        .filter(user -> user.getEmail().equals(userEmail))
                        .findFirst()
        );
    }

    @Override
    public boolean existsById(UUID uuid) {
        return load().map(users ->
                users.getUsersList().stream().anyMatch(user -> user.getId().equals(uuid))
        ).orElse(false);
    }

    private synchronized void saveToFile(Users users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("유저 데이터를 저장하는 데 실패했습니다.", e);
        }
    }

    private Optional<Users> load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return Optional.of((Users) ois.readObject());
        } catch (FileNotFoundException e) {
            return Optional.empty();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("유저 데이터를 불러오는 데 실패했습니다.", e);
        }
    }
}
