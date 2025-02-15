package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository extends FileRepository implements BinaryContentRepository {

   public FileBinaryContentRepository(@Value("${discodeit.repository.BinaryContent}") String fileDirectory) {
       super(fileDirectory);
   }
    @Override
    public void save(BinaryContent binaryContent) {
        Path path = resolvePath(binaryContent.getId());
        saveToFile(path,binaryContent);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        Path path = resolvePath(id);
        return loadFromFile(path);
    }

    @Override
    public Map<UUID, BinaryContent> findAll() {
        Map<UUID, BinaryContent> binaryContentMap = new HashMap<>();
        try (Stream<Path> pathStream = Files.walk(this.getDIRECTORY())) {
            pathStream.filter(path -> path.toString().endsWith(".ser")).forEach(path -> {
                Optional<BinaryContent> binaryContent = loadFromFile(path);
                binaryContent.ifPresent(bc -> binaryContentMap.put(bc.getId(), bc));
            });
        } catch (IOException e) {
            System.out.println("파일을 읽을 수 없습니다." + e.getMessage());
        }
        return binaryContentMap;
    }

    @Override
    public void delete(UUID id) {
        Path path = resolvePath(id);
        deleteFile(path);

    }

    @Override
    public boolean existsById(UUID userId) {
        Path path = resolvePath(userId);
        return Files.exists(path);
    }
}
