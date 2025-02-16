package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Repository
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
        return null;
    }

    @Override
    public List<BinaryContent> findAll() {
        return List.of();
    }

    @Override
    public void delete(UUID binaryContentId) {

    }

    @Override
    public boolean existsById(UUID binaryContentId) {
        return false;
    }
}
