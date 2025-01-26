package discodeit.repository.file;

import discodeit.entity.User;
import discodeit.repository.UserRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileUserRepository implements UserRepository {

    private final Path directory;

    public FileUserRepository() {
        this.directory = Paths.get("src", "main", "resources", "data", "serialized", "users");
    }

    @Override
    public User save(String name, String email, String phoneNumber, String password) {
        return null;
    }

    @Override
    public User find(UUID userId) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public void update(UUID userId, String name, String email, String phoneNumber) {

    }

    @Override
    public void updatePassword(UUID userId, String originalPassword, String newPassword) {

    }

    @Override
    public void delete(UUID userId) {

    }
}
