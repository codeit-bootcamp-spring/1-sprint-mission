package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.collection.Users;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserRepository implements UserRepository {
    private final String filePath;
    private Users users; // 🔥 users 인스턴스를 메모리에 유지

    public FileUserRepository(@Value("${file.path.user}") String filePath) {
        this.filePath = filePath;
        this.users = load().orElse(new Users());
    }

    @Override
    public synchronized User save(User newUser) {
        users.add(newUser.getId(), newUser);
        saveToFile();
        return newUser;
    }

    @Override
    public List<User> getAllUsers() {
        return users.getUsersList();
    }

    @Override
    public User getUserById(UUID id) {
        return users.get(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 사용자를 찾을 수 없습니다."));
    }

    @Override
    public synchronized void delete(UUID id) {
        users.remove(id);
        saveToFile();
    }

    @Override
    public void save() {
        saveToFile();
    }

    @Override
    public boolean existsByUsername(String username) {
        return users.getUsersList().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.getUsersList().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public Optional<User> getUserByEmail(String userEmail1) {
        return users.getUsersList().stream()
                .filter(user -> user.getEmail().equals(userEmail1))
                .findFirst();
    }

    private synchronized void saveToFile() {
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
