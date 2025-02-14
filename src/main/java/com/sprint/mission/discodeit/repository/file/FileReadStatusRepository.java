package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.file.FileService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileReadStatusRepository implements ReadStatusRepository {

    private final Path readStatusDirectory;

    public FileReadStatusRepository() {
        this.readStatusDirectory = Paths.get(System.getProperty("user.dir")).resolve("data/read-status");
        FileService.init(readStatusDirectory);
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Path readStatusPath = readStatusDirectory.resolve(readStatus.getId().concat(".json"));
        FileService.save(readStatusPath, readStatus);
        return null;
    }

    @Override
    public ReadStatus findById(String id) {
        Path readStatusPath = readStatusDirectory.resolve(id.concat(".json"));
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
        Path readStatusPath = readStatusDirectory.resolve(id.concat(".json"));
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
