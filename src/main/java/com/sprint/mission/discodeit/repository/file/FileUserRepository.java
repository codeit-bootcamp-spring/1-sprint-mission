package com.sprint.mission.discodeit.repository.file;

import static com.sprint.mission.discodeit.constant.ExitStatus.DIR_CREATION_ERROR;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.logger.repository.RepositoryLogger;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;

@Repository
public class FileUserRepository implements UserRepository {

    private static final RepositoryLogger logger = RepositoryLogger.getInstance();
    private static final Path FILE_DIR = Paths.get(System.getProperty("user.dir"), "file", "user");
    private static final String FILE_EXT = ".ser";

    static {
        if (!Files.exists(FILE_DIR)) {
            try {
                Files.createDirectories(FILE_DIR);
            } catch (IOException e) {
                System.err.println("Failed to mkdir: " + e.getMessage());
                System.exit(DIR_CREATION_ERROR.ordinal());
            }
        }
    }

    /**
     * Create the User while ignoring the {@code createAt} and {@code updateAt} fields from
     * {@code userInfoToCreate}
     */
    @Override
    public User createUser(User userInfoToCreate) {
        Path filePath = getFilePath(userInfoToCreate.id());
        if (Files.exists(filePath)) {
            return User.EMPTY_USER;
        }

        try (
            FileOutputStream fos = new FileOutputStream(filePath.toFile());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            User userToCreate = User.builder(
                    userInfoToCreate.name(), userInfoToCreate.email())
                .id(userInfoToCreate.id())
                .phoneNumber(userInfoToCreate.phoneNumber())
                .userStatus(userInfoToCreate.userStatus())
                .readStatus(userInfoToCreate.readStatus())
                .image(userInfoToCreate.image())
                .build();

            oos.writeObject(userToCreate);
            return userToCreate;
        } catch (IOException e) {
            logger.severe(e);
        }

        return User.EMPTY_USER;
    }

    @Override
    public User findUserById(UUID key) {
        Path filePath = getFilePath(key);
        if (!Files.exists(filePath)) {
            return User.EMPTY_USER;
        }

        try (
            FileInputStream fis = new FileInputStream(filePath.toFile());
            ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.severe(e);
        }

        return User.EMPTY_USER;
    }

    @Override
    public User findUserByName(String name) {
        try (Stream<Path> paths = Files.list(Paths.get("user"))) {
            for (Path path : paths.toList()) {
                try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
                ) {
                    User read = (User) ois.readObject();
                    if (read.name().equals(name)) {
                        return read;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    logger.severe(e);
                }
            }
        } catch (IOException e) {
            logger.severe(e);
        }

        return User.EMPTY_USER;
    }

    @Override
    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();
        try (Stream<Path> paths = Files.list(Paths.get("user"))) {
            for (Path path : paths.toList()) {
                try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
                ) {
                    users.add((User) ois.readObject());
                } catch (IOException | ClassNotFoundException e) {
                    logger.severe(e);
                }
            }
        } catch (IOException e) {
            logger.severe(e);
        }
        return users;
    }

    /**
     * Update the User while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields from
     * {@code userInfoToUpdate}
     */
    @Override
    public User updateUserById(UUID key, User userInfoToUpdate) {
        Path filePath = getFilePath(key);
        if (!Files.exists(filePath)) {
            return User.EMPTY_USER;
        }

        User exsitingUser = findUserById(key);
        try (
            FileOutputStream fis = new FileOutputStream(filePath.toFile());
            ObjectOutputStream ois = new ObjectOutputStream(fis);
        ) {
            User userToUpdate = User.builder(
                    userInfoToUpdate.name(), userInfoToUpdate.email())
                .id(key)
                .createAt(exsitingUser.createAt())
                .phoneNumber(userInfoToUpdate.phoneNumber())
                .userStatus(userInfoToUpdate.userStatus())
                .readStatus(userInfoToUpdate.readStatus())
                .image(userInfoToUpdate.image())
                .build();

            ois.writeObject(userToUpdate);
            return userToUpdate;
        } catch (IOException e) {
            logger.severe(e);
        }

        return User.EMPTY_USER;
    }

    @Override
    public User deleteUserById(UUID key) {
        Path filePath = getFilePath(key);
        if (!Files.exists(filePath)) {
            return User.EMPTY_USER;
        }

        User exsitingUser = findUserById(key);
        try {
            Files.delete(filePath);
            return exsitingUser;
        } catch (IOException e) {
            logger.severe(e);
        }

        return User.EMPTY_USER;
    }

    private Path getFilePath(UUID key) {
        return FILE_DIR.resolve(key.toString().concat(FILE_EXT));
    }
}
