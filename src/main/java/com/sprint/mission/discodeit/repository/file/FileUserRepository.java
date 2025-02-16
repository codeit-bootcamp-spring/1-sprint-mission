package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FileUserRepository implements UserRepository {
    public static final Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "data/user");

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
        init(DIRECTORY); // 초기화
        Path filePath = DIRECTORY.resolve(user.getId().toString().concat(".ser"));
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
        Path filePath = DIRECTORY.resolve(user.getId().toString().concat(".ser"));

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
        if (Files.exists(DIRECTORY)) {
            try {
                List<User> list = Files.list(DIRECTORY)
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
        Path filePath = DIRECTORY.resolve(user.getId().toString().concat(".ser"));
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
