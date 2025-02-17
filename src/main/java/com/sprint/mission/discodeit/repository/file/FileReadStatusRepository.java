package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    private final Path readStatusDirectory;
    private final String extension = ".ser";

    public FileReadStatusRepository(@Value("${discodeit.repository.file-directory}") String fileDirectory) {
        this.readStatusDirectory = Paths.get(fileDirectory).resolve("read-status");
        FileService.init(readStatusDirectory);
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Path readStatusPath = readStatusDirectory.resolve(readStatus.getId().concat(extension));
        FileService.save(readStatusPath, readStatus);
        return null;
    }

    @Override
    public ReadStatus findById(String id) {
        Path readStatusPath = readStatusDirectory.resolve(id.concat(extension));
        List<ReadStatus> load = FileService.load(readStatusPath);
        if (load == null || load.isEmpty()) {
            return null;
        }
        return load.get(0);
    }

    @Override
    public List<ReadStatus> findAll() {
        return FileService.load(readStatusDirectory);
    }

    @Override
    public boolean delete(String id) {
        Path readStatusPath = readStatusDirectory.resolve(id.concat(extension));
        return FileService.delete(readStatusPath);
    }

    @Override
    public ReadStatus findByChannelIdWithUserId(String channelId, String userId) {
        return findAll().stream().filter(rs -> rs.getChannelId().equals(channelId) && rs.getUserId().equals(userId)).findFirst().orElse(null);
    }

    @Override
    public List<ReadStatus> findAllByUserId(String userId) {
        return findAll().stream().filter(rs -> rs.getUserId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public List<ReadStatus> findAllByChannelId(String channelId) {
        return findAll().stream().filter(rs -> rs.getChannelId().equals(channelId)).collect(Collectors.toList());
    }
}
