package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static final Path filePath = Paths.get(System.getProperty("user.dir"), "tmp/user.ser");

    @Override
    public User save(User user) {
        Map<UUID, User> savedUserMap = loadUserMapToFile();
        savedUserMap.put(user.getId(), user);
        saveUserMapToFile(savedUserMap);
        return user;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        Map<UUID, User> savedUserMap = loadUserMapToFile();
        return Optional.ofNullable(savedUserMap.get(userId));
    }

    @Override
    public List<User> findAll() {
        return loadUserMapToFile().values().stream().toList();
    }

    @Override
    public void delete(UUID userId) {
        Map<UUID, User> savedUserMap = loadUserMapToFile();
        savedUserMap.remove(userId);
        saveUserMapToFile(savedUserMap);
    }

    private void saveUserMapToFile(Map<UUID, User> saveUserMap) {
        List<User> saveUserList = saveUserMap.values().stream().toList();
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile(),false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            for (User user : saveUserList) {
                oos.writeObject(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<UUID, User> loadUserMapToFile() {
        Map<UUID, User> data = new HashMap<>();
        if(!Files.exists(filePath)) {
            return data;
        }
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                User serUser = (User) ois.readObject();
                data.put(serUser.getId(), serUser);
            }
        } catch (EOFException e) {
//            System.out.println("read all objects");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
