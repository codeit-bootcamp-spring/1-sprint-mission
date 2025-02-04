package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileUserRepository implements UserRepository {
    public Path directory = Paths.get(System.getProperty("user.dir"), "data/user");

    // 초기화
    public void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException("Directory could not be created. directory : " + directory, e);
            }
        }
    }

    // 저장
    public boolean saveUser(User user) {
        init(directory); // 초기화
        Path filePath = directory.resolve(user.getId().toString().concat(".ser"));
        if (Files.exists(filePath)) {
            deleteUser(user);
        }

        try(
                FileOutputStream fos = new FileOutputStream(filePath.toFile(), false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("File could not be saved.", e);
        }
    }

    // 조회
    public User loadUser(User user) {
        Path filePath = directory.resolve(user.getId().toString().concat(".ser"));

        if (Files.exists(filePath)) {
            try (
                    FileInputStream fis = new FileInputStream(filePath.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ) {
                return (User) ois.readObject();
            }  catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("File could not be loaded.", e);
            }
        } else {
            return null;
        }
    }

    public List<User> loadAllUser() {
        if (Files.exists(directory)) {
            try {
                List<User> list = Files.list(directory)
                        .map(path -> {
                            try (
                                    FileInputStream fis = new FileInputStream(path.toFile());
                                    ObjectInputStream ois = new ObjectInputStream(fis)
                            ) {
                                return (User) ois.readObject();
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException("File could not be loaded.", e);
                            }
                        })
                        .toList();
                return list;
            } catch (IOException e) {
                throw new RuntimeException("Failed to retrieve file list.", e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    // 삭제
    public boolean deleteUser(User user) {
        Path filePath = directory.resolve(user.getId().toString().concat(".ser"));
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException("File could not be deleted.", e);
        }
        return false;
    }

}
