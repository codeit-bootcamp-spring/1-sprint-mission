package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {

    private static final FileUserService fileUserService = new FileUserService();
    private static final Path filePath;

    static {
        filePath = Path.of(System.getProperty("user.dir"), "users");
        createDirectory();
    }

    private FileUserService() {}

    public static FileUserService getInstance() {
        return fileUserService;
    }

    private static void createDirectory() {
        if (!Files.exists(FileUserService.filePath)) {
            try {
                Files.createDirectories(FileUserService.filePath);
            } catch (IOException e) {
                throw new FileIOException("저장 디렉토리 생성 실패: " + filePath);
            }
        }
    }

    @Override
    public User createUser(UserDto userDto) {
        String password = generatePassword(userDto.getPassword());
        User user = User.of(userDto.getName(), userDto.getLoginId(), password);
        return writeUserToFile(user);
    }

    private User writeUserToFile(User user) {
        Path path = filePath.resolve(user.getId().toString().concat(".ser"));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(user);
            return user;
        } catch (IOException e) {
            throw new FileIOException("user 저장 실패");
        }
    }

    @Override
    public User readUser(UUID userId) {
        Path path = filePath.resolve(userId.toString().concat(".ser"));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (User)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NotFoundException("등록되지 않은 user입니다.");
        }
    }

    @Override
    public List<User> readAll() {
        try {
            return Files.list(filePath)
                .map(path -> {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                        Object data = ois.readObject();
                        return (User) data;
                    } catch (IOException | ClassNotFoundException e) {
                        throw new FileIOException("users 읽기 실패");
                    }
                })
                .toList();
        } catch (IOException e) {
            throw new FileIOException("users 읽기 실패");
        }
    }

    @Override
    public void updateUser(UUID userId, UserDto userDto) {
        User user = readUser(userId);
        user.updateName(userDto.getName());
        user.updatePassword(generatePassword(userDto.getPassword()));
        user.updateLoginId(userDto.getLoginId());
        writeUserToFile(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        Path path = filePath.resolve(userId.toString().concat(".ser"));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("user 삭제 실패");
            }
        }
    }

    private String generatePassword(String password) {
        StringBuilder builder = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes());

            for (byte b : digest) {
                builder.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("비밀번호 암호화 오류");
        }
        return builder.toString();
    }
}
