package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.file.FileService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileUserStatusRepository implements UserStatusRepository {

    private final Path userStatusDirectroy;

    public FileUserStatusRepository() {
        this.userStatusDirectroy = Paths.get(System.getProperty("user.dir")).resolve("data/user-status");
        FileService.init(this.userStatusDirectroy);
    }

    @Override
    public UserStatus findById(String id) {
        Path userStatusPath = userStatusDirectroy.resolve(id.concat(".ser"));
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
        Path userStatusPath = userStatusDirectroy.resolve(userStatus.getId().concat(".ser"));
        FileService.save(userStatusPath, userStatus);
        return userStatus;
    }

    @Override
    public boolean delete(String id) {
        Path userStatusPath = userStatusDirectroy.resolve(id.concat(".ser"));
        return FileService.delete(userStatusPath);
    }
}
