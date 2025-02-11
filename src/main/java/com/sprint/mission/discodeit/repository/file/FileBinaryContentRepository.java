package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.sprint.mission.discodeit.util.FileIOUtil.loadFromFile;
import static com.sprint.mission.discodeit.util.FileIOUtil.saveToFile;

@Repository
@Primary
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path filePath = Path.of("./result/binarycontent.ser");
    private final Map<UUID, BinaryContent> repository;

    public FileBinaryContentRepository() {
        if (!Files.exists(this.filePath)) {
            try {
                Files.createFile(this.filePath);
                saveToFile(new HashMap<>(), this.filePath);
            } catch (IOException e) {
                throw new RuntimeException("회원 파일을 초기화 하던 중에 문제가 발생했습니다", e);
            }
        }
        this.repository = loadFromFile(filePath);
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        repository.put(binaryContent.getId(), binaryContent);
        saveToFile(repository, filePath);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findById(UUID uuid) {
        return repository.values().stream()
                .filter(binaryContent -> binaryContent.getId().equals(uuid))
                .findFirst();
    }

    @Override
    public List<BinaryContent> findAll() {
        return repository.values().stream().toList();
    }

    @Override
    public void deleteById(UUID uuid) {
        repository.remove(uuid);
        saveToFile(repository, filePath);
    }
}
