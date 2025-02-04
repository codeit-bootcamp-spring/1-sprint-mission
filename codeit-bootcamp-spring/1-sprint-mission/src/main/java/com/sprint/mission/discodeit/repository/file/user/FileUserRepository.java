package com.sprint.mission.discodeit.repository.file.user;

import com.sprint.mission.discodeit.entity.user.entity.User;
import com.sprint.mission.discodeit.repository.file.FileAbstractRepository;
import com.sprint.mission.discodeit.repository.jcf.user.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class FileUserRepository extends FileAbstractRepository<User, UUID> implements UserRepository {
    private static final String FILE_PATH_USER_NAME = "temp/file/user/user.ser";
    private static UserRepository FILE_USER_REPOSITORY_INSTANCE;

    private FileUserRepository() {
        super(FILE_PATH_USER_NAME);
        store.putAll(loadFile());
    }

    public static UserRepository getInstance() {
        if (FILE_USER_REPOSITORY_INSTANCE == null) {
            FILE_USER_REPOSITORY_INSTANCE = new FileUserRepository();
        }
        return FILE_USER_REPOSITORY_INSTANCE;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        var findUser = findAll().stream()
                .filter(user -> username.equals(user.getName()))
                .findFirst();

        return findUser;
    }

}
