package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.config.FileRepositoryCondition;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@FileRepositoryCondition
public class FileUserStatusRepository implements UserStatusRepository {

    private final FileStorage fileStorage;
    private final Path directory;
    private static final String SUBDIRECTORY = "userStatus";

    public FileUserStatusRepository(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
        directory = fileStorage.getDirectory(SUBDIRECTORY);
        fileStorage.init(directory);
    }

    @Override
    public UserStatus saveUserStatus(UserStatus userStatus) {
        Path filePath = fileStorage.getFilePath(directory, userStatus.getId());
        return fileStorage.save(filePath, userStatus);
    }

    @Override
    public UserStatus findUserStatusById(UUID userStatusId) {
        List<UserStatus> userStatusList = fileStorage.load(directory);

        return userStatusList.stream()
                .filter(userStatus -> userStatus.getId().equals(userStatusId))
                .findFirst().orElse(null);
    }

    @Override
    public Optional<UserStatus> findUserStatusByUser(User user) {
        List<UserStatus> userStatusList = fileStorage.load(directory);

        return userStatusList.stream()
                .filter(userStatus -> userStatus.getUser().equals(user))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAllUserStatuses() {
        return fileStorage.load(directory);
    }

    @Override
    public void removeUserStatus(UUID userStatusId) {
        UserStatus userStatus = findUserStatusById(userStatusId);
        fileStorage.remove(directory, userStatus);
    }
}
