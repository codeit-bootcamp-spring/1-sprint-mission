package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Repository
@Scope("singleton")
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path directory;

    public FileBinaryContentRepository() {
        this.directory = Paths.get("src", "main", "resources", "data", "serialized", "binaryContents");
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Path filePath = directory.resolve(binaryContent.getId() + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return binaryContent;
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        Path filePath = directory.resolve(binaryContentId + ".ser");
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            Object data = ois.readObject();
            return (BinaryContent) data;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BinaryContent> findAll() {
        try {
            List<BinaryContent> binaryContents = Files.list(directory)
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ) {
                            Object data = ois.readObject();
                            return (BinaryContent) data;
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            return binaryContents;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UUID binaryContentId) {
        Path filePath = directory.resolve(binaryContentId + ".ser");

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            System.out.println("삭제에 실패하였습니다.");
        }
    }

    @Override
    public boolean existsById(UUID binaryContentId) {
        Path filePath = directory.resolve(binaryContentId + ".ser");
        return Files.exists(filePath);
    }
}
