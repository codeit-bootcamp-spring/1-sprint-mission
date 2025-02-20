package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Repository
@ConditionalOnProperty(name="discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileUserRepository implements UserRepository {

    private final Path directory;
    private final String extension;

    public FileUserRepository(RepositoryProperties properties) {
        this.directory = Paths.get(System.getProperty(properties.getBaseDirectory()))
                .resolve(properties.getFileDirectory())
                .resolve("user");
        this.extension = properties.getExtension();
        FileService.init(directory);
    }

    @Override
    public User save(User user) {
        Path userPath = directory.resolve(user.getId().concat(extension));
        FileService.save(userPath, user);
        return user;
    }

    @Override
    public boolean delete(User user) {
        Path userPath = directory.resolve(user.getId().concat(extension));
        return FileService.delete(userPath);
    }

    @Override
    public User findById(String id) {
        Path userPath = directory.resolve(id.concat(extension));
        return (User) FileService.read(userPath);
    }

    @Override
    public List<User> findAll() {
        return FileService.load(Paths.get(directory.toString()));
    }
}
