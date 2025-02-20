package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Repository
@ConditionalOnProperty(name="discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileUserStatusRepository implements UserStatusRepository {

    private final Path directory;
    private final String extension;

    public FileUserStatusRepository(RepositoryProperties properties) {
        this.directory = Paths.get(System.getProperty(properties.getBaseDirectory()))
                .resolve(properties.getFileDirectory())
                .resolve("user-status");
        this.extension = properties.getExtension();
        FileService.init(this.directory);
    }

    @Override
    public UserStatus findById(String id) {
        Path userStatusPath = directory.resolve(id.concat(extension));
        return (UserStatus) FileService.read(userStatusPath);
    }

    @Override
    public List<UserStatus> findAll() {
        return FileService.load(directory);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path userStatusPath = directory.resolve(userStatus.getId().concat(extension));
        FileService.save(userStatusPath, userStatus);
        return userStatus;
    }

    @Override
    public boolean delete(String id) {
        Path userStatusPath = directory.resolve(id.concat(extension));
        return FileService.delete(userStatusPath);
    }
}
