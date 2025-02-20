package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name="discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileReadStatusRepository implements ReadStatusRepository {

    private final Path directory;
    private final String extension;

    public FileReadStatusRepository(RepositoryProperties properties) {
        this.directory = Paths.get(System.getProperty(properties.getBaseDirectory()))
                .resolve(properties.getFileDirectory())
                .resolve("read-status");
        this.extension = properties.getExtension();
        FileService.init(this.directory);
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Path readStatusPath = directory.resolve(readStatus.getId().concat(extension));
        FileService.save(readStatusPath, readStatus);
        return null;
    }

    @Override
    public ReadStatus findById(String id) {
        Path readStatusPath = directory.resolve(id.concat(extension));
        return (ReadStatus) FileService.read(readStatusPath);
    }

    @Override
    public List<ReadStatus> findAll() {
        return FileService.load(directory);
    }

    @Override
    public boolean delete(String id) {
        Path readStatusPath = directory.resolve(id.concat(extension));
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
