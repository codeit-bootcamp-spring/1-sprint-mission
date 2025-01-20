package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.log.RepositoryLogger;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.ExitStatus.DIR_CREATION_ERROR;

public class FileUserRepository implements UserRepository {
    private static final RepositoryLogger logger = RepositoryLogger.getInstance();
    private static final Path   FILE_DIR  = Paths.get(System.getProperty("user.dir"), "file", "user");
    private static final String FILE_EXT  = ".ser";

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
     * Create the User while ignoring the {@code createAt} and {@code updateAt} fields from {@code userInfoToCreate}
     */
    @Override
    public User createUser(User userInfoToCreate) {
        Path filePath = getFilePath(userInfoToCreate.getId());
        if (Files.exists(filePath)) {
            return User.createEmptyUser();
        }

        try (
                FileOutputStream   fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            User userToCreate = new User.Builder(
                    userInfoToCreate.getName(), userInfoToCreate.getEmail())
                    .id(userInfoToCreate.getId())
                    .phoneNumber(userInfoToCreate.getPhoneNumber())
                    .build();

            oos.writeObject(userToCreate);
            return userToCreate;
        } catch (IOException e) {
            logger.severe(e);
        }

        return User.createEmptyUser();
    }

    @Override
    public User findUserById(UUID key) {
        Path filePath = getFilePath(key);
        if (!Files.exists(filePath)) {
            return User.createEmptyUser();
        }

        try (
                FileInputStream   fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.severe(e);
        }

        return User.createEmptyUser();
    }

    /**
     * Update the User while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields from {@code userInfoToUpdate}
     */
    @Override
    public User updateUserById(UUID key, User userInfoToUpdate) {
        Path filePath = getFilePath(key);
        if (!Files.exists(filePath)) {
            return User.createEmptyUser();
        }

        User exsitingUser = findUserById(key);
        try (
                FileOutputStream   fis = new FileOutputStream(filePath.toFile());
                ObjectOutputStream ois = new ObjectOutputStream(fis);
        ) {
            User userToUpdate = new User.Builder(
                    userInfoToUpdate.getName(), userInfoToUpdate.getEmail())
                    .id(key)
                    .createAt(exsitingUser.getCreateAt())
                    .phoneNumber(userInfoToUpdate.getPhoneNumber())
                    .build();

            ois.writeObject(userToUpdate);
            return userToUpdate;
        } catch (IOException e) {
            logger.severe(e);
        }

        return User.createEmptyUser();
    }

    @Override
    public User deleteUserById(UUID key) {
        Path filePath = getFilePath(key);
        if (!Files.exists(filePath)) {
            return User.createEmptyUser();
        }

        User exsitingUser = findUserById(key);
        try {
            Files.delete(filePath);
            return exsitingUser;
        } catch (IOException e) {
            logger.severe(e);
        }

        return User.createEmptyUser();
    }

    private Path getFilePath(UUID key) {
        return FILE_DIR.resolve(key.toString().concat(FILE_EXT));
    }
}
