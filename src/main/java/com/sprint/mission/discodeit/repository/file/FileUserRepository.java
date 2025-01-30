package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileUserRepository {
    Path directory = Paths.get(System.getProperty("user.dir"), "data");
    Path filePath = directory.resolve("users.ser");

    public void init() {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                System.out.println("디렉토리 생성 완료");
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage(), e);
            }
        }

    }

    public void save(List<User> users) {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
                oos.writeObject(users);
            } catch (IOException e) {
                throw new RuntimeException("사용자 저장 실패: " + e.getMessage(), e);
            }
    }

    @SuppressWarnings("unchecked")
    public List<User> load() {
        List<User> users = new ArrayList<>();
        Path filePath = directory.resolve("users.ser");

        if (Files.exists(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
                users = (List<User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("사용자 로드 실패: " + e.getMessage(), e);
            }
        }

        return users;
    }
}
