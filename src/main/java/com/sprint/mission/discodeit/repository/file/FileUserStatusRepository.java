package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
@Repository
public class FileUserStatusRepository implements UserStatusRepository {

    private final Path userStatusDirectroy;
    private final String extension = ".ser";

    public FileUserStatusRepository(@Value("${discodeit.repository.file-directory}") String fileDirectory) {
        this.userStatusDirectroy = Paths.get(fileDirectory).resolve("userStatus");
        FileService.init(this.userStatusDirectroy);
    }

    @Override
    public UserStatus findById(String id) {
        Path userStatusPath = userStatusDirectroy.resolve(id.concat(extension));
        List<UserStatus> load = FileService.load(userStatusPath);
        if( load == null || load.isEmpty()){
            return null;
        }
        return load.get(0);
    }

    @Override
    public List<UserStatus> findAll() {
        return FileService.load(userStatusDirectroy);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path userStatusPath = userStatusDirectroy.resolve(userStatus.getId().concat(extension));
        FileService.save(userStatusPath, userStatus);
        return userStatus;
    }

    @Override
    public boolean delete(String id) {
        Path userStatusPath = userStatusDirectroy.resolve(id.concat(extension));
        return FileService.delete(userStatusPath);
    }
}
