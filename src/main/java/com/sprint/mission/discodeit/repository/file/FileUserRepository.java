package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
@Repository
public class FileUserRepository implements UserRepository {

    private final Path userDirectory;
    private final String extension = ".ser";


    public FileUserRepository(@Value("${discodeit.repository.file-directory}") String fileDirectory) {
        this.userDirectory = Paths.get(fileDirectory).resolve("users");
        FileService.init(userDirectory);
    }

    @Override
    public User save(User user) {
        Path userPath = userDirectory.resolve(user.getId().concat(extension));
        FileService.save(userPath, user);
        return user;
    }

    @Override
    public boolean delete(User user) {
        Path userPath = userDirectory.resolve(user.getId().concat(extension));
        return FileService.delete(userPath);
    }

    @Override
    public User findById(String id) {
        Path userPath = userDirectory.resolve(id.concat(extension));
        return (User) FileService.load(userPath);
    }


    @Override
    public List<User> findAll() {
        return FileService.load(Paths.get(userDirectory.toString()));
    }
}
