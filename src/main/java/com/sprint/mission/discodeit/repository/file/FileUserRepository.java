package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {
    private final String filePath = "users.dat";

    @Override
    public User save(User user) {
        Map<String, User> users = readFromFile();
        users.put(user.getId(), user);
        writeToFile(users);
        return user;
    }

    @Override
    public void deleteById(String id) {
        Map<String, User> users = readFromFile();
        users.remove(id);
        writeToFile(users);
    }

    @Override
    public Optional<User> findById(String id) {
        Map<String, User> users = readFromFile();
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(readFromFile().values());
    }

    private Map<String, User> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void writeToFile(Map<String, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}