package some_path._1sprintmission.discodeit.repository.file;

import some_path._1sprintmission.discodeit.entiry.BinaryContent;
import some_path._1sprintmission.discodeit.repository.BinaryContentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileBinaryContentRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", BinaryContent.class.getSimpleName());
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

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Path path = resolvePath(binaryContent.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save BinaryContent", e);
        }
        return binaryContent;
    }

    @Override
    // ✅ id로 조회
    public Optional<BinaryContent> findById(UUID id) {
        Path path = resolvePath(id);
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        try (
                FileInputStream fis = new FileInputStream(path.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            return Optional.of((BinaryContent) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load BinaryContent", e);
        }
    }

    @Override
    // ✅ id 목록으로 조회
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return ids.stream()
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    // ✅ id로 삭제
    public void deleteById(UUID id) {
        Path path = resolvePath(id);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete BinaryContent", e);
        }
    }

    @Override
    public Optional<BinaryContent> findByMessageId(UUID messageId) {
        try (Stream<Path> files = Files.list(DIRECTORY)) {
            return files
                    .filter(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            BinaryContent binaryContent = (BinaryContent) ois.readObject();
                            return binaryContent.getMessage().getId().equals(messageId);
                        } catch (IOException | ClassNotFoundException e) {
                            return false;
                        }
                    })
                    .findFirst()
                    .map(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            return (BinaryContent) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("Failed to read BinaryContent", e);
                        }
                    });
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        try (Stream<Path> files = Files.list(DIRECTORY)) {
            files.filter(path -> {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                    BinaryContent binaryContent = (BinaryContent) ois.readObject();
                    return binaryContent.getMessage().getId().equals(messageId);
                } catch (IOException | ClassNotFoundException e) {
                    return false;
                }
            }).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete BinaryContent", e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete BinaryContent", e);
        }
    }
}

