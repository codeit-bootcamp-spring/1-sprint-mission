package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
public class FileReadStatusRepository extends FileRepository implements ReadStatusRepository {
    public FileReadStatusRepository(@Value("${discodeit.repository.readStatus}") String fileDirectory){
        super(fileDirectory);
    }

    @Override
    public void save(ReadStatus readStatus) {
        Path path = resolvePath(readStatus.getId());
        saveToFile(path,readStatus);

    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        Path path = resolvePath(id);
        return loadFromFile(path);
    }

    @Override
    public Map<UUID, ReadStatus> findAll() {
        Map<UUID, ReadStatus> readStatusMap = new HashMap<>();
        try (Stream<Path> pathStream = Files.walk(getDIRECTORY())) {
            pathStream.filter(path -> path.toString().endsWith(".ser"))
                    .forEach(path -> {
                        Optional<ReadStatus>readStatus = loadFromFile(path);
                        readStatus.ifPresent(rs -> readStatusMap.put(rs.getId(), rs));
                    });
        }catch (IOException e) {
            System.out.println("파일을 읽을 수 없습니다." + e.getMessage());
        }
        return readStatusMap;
    }

    @Override
    public void delete(UUID id) {
        Path path = resolvePath(id);
        deleteFile(path);

    }
}
