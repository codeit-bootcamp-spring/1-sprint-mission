package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Repository
@ConditionalOnProperty(name = "sprint-mission.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {

    private static final String FILE_PATH = "files/users.ser";
    private Map<UUID, User> users;

    public FileUserRepository() {
        this.users = loadFromFile();
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        saveToFile();
        return user;
    }

    @Override
    public User findByUsername(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findById(UUID id) {
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return users.containsKey(id);
    }

//    @Override
//    public boolean existsByEmail(String email) {
//        for (User user : users.values()) {
//            if (user.getEmail().equals(email)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public boolean existsByUsername(String username) {
//        for (User user : users.values()) {
//            if (user.getUsername().equals(username)) {
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    public void deleteById(UUID id) {
        users.remove(id);
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<UUID, User> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}