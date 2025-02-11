package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserStatusRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", "UserStatus");
        if (Files.notExists(this.DIRECTORY)) {
            try {
                Files.createDirectories(this.DIRECTORY, new FileAttribute<?>[0]);
            } catch (IOException e) {
                throw new RuntimeException("UserStatus 저장 디렉토리 생성 실패", e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return this.DIRECTORY.resolve(id.toString() + EXTENSION);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path path = resolvePath(userStatus.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(userStatus);
        } catch (IOException e) {
            throw new RuntimeException("UserStatus 저장 실패", e);
        }
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        Path path = resolvePath(id);
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return Optional.of((UserStatus) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("UserStatus 조회 실패", e);
        }
    }

    @Override
    public List<UserStatus> findAll() {
        try {
            return Files.list(this.DIRECTORY)
                    .map(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            return (UserStatus) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("UserStatus 목록 조회 실패", e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("UserStatus 목록 조회 실패", e);
        }
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatus updatedStatus) {
        Path path = resolvePath(userId);
        if (!Files.exists(path)) {
            // 영어 문장을 한글로 변경
            throw new RuntimeException("해당 userId로 UserStatus를 찾을 수 없습니다: " + userId);
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(updatedStatus);
        } catch (IOException e) {
            throw new RuntimeException("UserStatus 업데이트 실패", e);
        }
        return updatedStatus;
    }

    @Override
    public void deleteById(UUID id) {
        try {
            Files.deleteIfExists(resolvePath(id));
        } catch (IOException e) {
            throw new RuntimeException("UserStatus 삭제 실패", e);
        }
    }
}
