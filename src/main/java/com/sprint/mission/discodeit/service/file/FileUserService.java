package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.util.FileIOUtil.*;

public class FileUserService implements UserService {
    private final Path filePath = Path.of("./result/users.ser");
    private final Map<UUID, User> users = loadFromFile(filePath);

    public FileUserService() {
        if (!Files.exists(this.filePath)) {
            try {
                Files.createFile(this.filePath);
                saveToFile(new HashMap<>(), this.filePath);
            } catch (IOException e) {
                throw new RuntimeException("회원 파일을 초기화 하던 중에 문제가 발생했습니다", e);
            }
        }
    }

    @Override
    public User create(String name, String phone, String password) {
        if (!User.isValidPassword(password)) {
            throw new ServiceException(ErrorCode.INVALID_PASSWORD);
        }

        if (!User.isValidPhone(phone)) {
            throw new ServiceException(ErrorCode.INVALID_WRITER);
        }
        if (users.values().stream()
                .anyMatch(user -> user.getPhone().equals(phone))) {
            throw new ServiceException(ErrorCode.CANNOT_FOUND_USER);
        }

        User createUser = new User(name, phone, password);
        users.put(createUser.getId(), createUser);
        saveToFile(users, filePath);
        return createUser;
    }

    @Override
    public Optional<User> getUserByPhone(String phone) {
        return users.values().stream()
                .filter(user -> user.getPhone().equals(phone))
                .findFirst();
    }

    @Override
    public List<User> getAllUser() {
        return users.values()
                .stream()
                .collect(Collectors.toList());
    }


    @Override
    public User updateUserPassword(User updateUser, String newPass) {
        if (!userExists(updateUser.getPhone())) {
            throw new ServiceException(ErrorCode.CANNOT_FOUND_USER);
        }
        updateUser.update(newPass);
        users.put(updateUser.getId(), updateUser);
        saveToFile(users, filePath);
        return updateUser;
    }

    @Override
    public void delete(User removeUser) {
        if (!userExists(removeUser.getPhone())) {
            throw new ServiceException(ErrorCode.CANNOT_FOUND_USER);
        }
        users.remove(removeUser.getId());
        saveToFile(users, filePath);
    }

    private boolean userExists(String phone) {
        return users.values()
                .stream()
                .anyMatch(user -> user.getPhone().equals(phone));
    }
}
