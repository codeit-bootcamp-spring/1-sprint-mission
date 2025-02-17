package some_path._1sprintmission.discodeit.repository.file;

import org.springframework.stereotype.Repository;
import some_path._1sprintmission.discodeit.entiry.ReadStatus;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.repository.ReadStatusRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileReadStatusRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", ReadStatus.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id.toString() + EXTENSION);
    }

    private Path resolvePath(UUID userId, UUID channelId) {
        return DIRECTORY.resolve(userId.toString() + "_" + channelId.toString() + EXTENSION);
    }
    @Override
    public Optional<ReadStatus> findById(UUID id) {
        Path path = resolvePath(id);
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return Optional.of((ReadStatus) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load ReadStatus", e);
        }
    }

    @Override
    // ✅ 특정 userId를 조건으로 ReadStatus 목록 조회
    public List<ReadStatus> findAllByUserId(UUID userId) {
        List<ReadStatus> result = new ArrayList<>();
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            ReadStatus readStatus = (ReadStatus) ois.readObject();
                            if (readStatus.getUser().getId().equals(userId)) {
                                result.add(readStatus);
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("Failed to read ReadStatus", e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to list ReadStatus files", e);
        }
        return result;
    }

    @Override
    // ✅ ReadStatus 업데이트 (덮어쓰기)
    public ReadStatus update(ReadStatus readStatus) {
        return save(readStatus); // 동일한 파일에 덮어쓰기
    }

    // ✅ id로 ReadStatus 삭제
    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete ReadStatus", e);
        }
    }

    @Override
    // 기존 save 메서드 (생성 및 업데이트용)
    public ReadStatus save(ReadStatus readStatus) {
        Path path = resolvePath(readStatus.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(readStatus);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save ReadStatus", e);
        }
        return readStatus;
    }
    @Override
    public void deleteByUser(User user) {
        try (Stream<Path> files = Files.list(DIRECTORY)) {
            files.filter(path -> {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                    ReadStatus status = (ReadStatus) ois.readObject();
                    return status.getUser().equals(user);
                } catch (IOException | ClassNotFoundException e) {
                    return false;
                }
            }).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        Path path = resolvePath(null,channelId);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("읽음 상태 삭제 실패", e);
        }
    }

    @Override
    public Optional<ReadStatus> findByUserAndChannel(UUID userId, UUID channelId) {
        Path path = resolvePath(userId, channelId);
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return Optional.of((ReadStatus) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load ReadStatus", e);
        }
    }

}

