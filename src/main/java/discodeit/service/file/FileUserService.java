package discodeit.service.file;

import discodeit.entity.User;
import discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
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
        return user;
    }

    @Override
    public User find(UUID userId) {
        Path filePath = directory.resolve(userId + ".ser");
        try (
            FileInputStream fis = new FileInputStream(filePath.toFile());
            ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            Object data = ois.readObject();
            return (User) data;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try {
            List<User> users = Files.list(directory)
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ) {
                            Object data = ois.readObject();
                            return (User) data;
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            return users;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getInfo(UUID userId) {
        User user = find(userId);
        return user.toString();
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
