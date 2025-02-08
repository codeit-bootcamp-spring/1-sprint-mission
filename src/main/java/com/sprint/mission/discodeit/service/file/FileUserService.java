package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUserService implements UserService {

    private final Map<UUID, User> data = new HashMap<>();
    private final UserValidator userValidator;
    private static final Logger LOGGER = Logger.getLogger(FileUserRepository.class.getName());
    private final Path directory = Paths.get(System.getProperty("user.dir"), "Data/user_data");
    private final String fileName = "user_data.ser";

    public FileUserService(UserValidator userValidator) {
        this.userValidator = userValidator;
        init(directory);
        loadDataFromFile();
    }

    @Override
    public User create(String username, String email, String password) {
        if (userValidator.validateUser(username, email, password, findAll())) {
            User user = new User(username, email, password);
            data.put(user.getId(), user);
            saveDataToFile();
            return user;
        }
        throw new CustomException(ExceptionText.USER_CREATION_FAILED);
    }

    @Override
    public User find(UUID userId) {
        return data.get(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = find(userId);
        if (user != null) {
            user.update(newUsername,newEmail,newPassword);
        }
        throw new CustomException(ExceptionText.USER_NOT_FOUND);
    }

    @Override
    public void delete(UUID userId) {
        if (data.remove(userId) != null) {
            saveDataToFile();
        } else {
            throw new CustomException(ExceptionText.USER_NOT_FOUND);
        }
    }

    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage());
            }
        }
    }

    private void saveDataToFile() {
        Path filePath = directory.resolve(fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage());
        }
    }

    private void loadDataFromFile() {
        Path filePath = directory.resolve(fileName);
        if (Files.exists(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
                Object readObject = ois.readObject();
                if (readObject instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<UUID, User> loadedData = (Map<UUID, User>) readObject;
                    data.putAll(loadedData);
                } else {
                    throw new RuntimeException("잘못된 데이터 형식");
                }
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.WARNING, "파일 로드 실패", e);
            }
        }
    }
}
