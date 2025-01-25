package discodeit.service.file;

import discodeit.entity.User;
import discodeit.service.UserService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {

    private final Path directory;

    public FileUserService() {
        directory = Paths.get("src", "main", "resources", "data", "serialized", "users");
    }

    @Override
    public User create(String name, String email, String phoneNumber, String password) {
        User user = new User(name, email, phoneNumber, password);
        Path filePath = directory.resolve(user.getId() + ".ser");
        try (
            FileOutputStream fos = new FileOutputStream(filePath.toFile());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public String getInfo(User user) {
        return "";
    }

    @Override
    public void update(UUID userId, String name, String email, String phoneNumber) {

    }

    @Override
    public void updatePassword(User user, String originalPassword, String newPassword) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void isDuplicateEmail(String email) {

    }

    @Override
    public void isDuplicatePhoneNumber(String phoneNumber) {

    }
}
