package some_path._1sprintmission.discodeit.repository.file;

import org.springframework.stereotype.Repository;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.entiry.UserStatus;
import some_path._1sprintmission.discodeit.repository.UserStatusRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserStatusRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", UserStatus.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory for UserStatus storage", e);
            }
        }
    }

    private Path resolvePath(UUID userId) {
        return DIRECTORY.resolve(userId.toString() + EXTENSION);
    }

    @Override
    // ✅ 저장
    public UserStatus save(UserStatus userStatus) {
        Path path = resolvePath(userStatus.getUser().getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(userStatus);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save UserStatus", e);
        }
        return userStatus;
    }

    @Override
    // ✅ id로 조회
    public Optional<UserStatus> findById(UUID userId) {
        Path path = resolvePath(userId);
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return Optional.of((UserStatus) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load UserStatus", e);
        }
    }

    @Override
    // ✅ 전체 조회
    public List<UserStatus> findAll() {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (UserStatus) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("Failed to load UserStatus", e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list UserStatus files", e);
        }
    }

    @Override
    // ✅ userId로 업데이트
    public UserStatus updateByUserId(UUID userId, Instant newLastSeenAt) {
        Optional<UserStatus> existing = findById(userId);
        if (existing.isEmpty()) {
            throw new RuntimeException("UserStatus not found for userId: " + userId);
        }
        UserStatus updatedStatus = new UserStatus(existing.get().getUser(), newLastSeenAt);
        return save(updatedStatus);
    }

    @Override
    // ✅ id로 삭제
    public void deleteById(UUID userId) {
        Path path = resolvePath(userId);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete UserStatus", e);
        }
    }
}
