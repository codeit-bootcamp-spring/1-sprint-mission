package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileUserService implements UserService {
    private static final Path filePath = Paths.get(System.getProperty("user.dir"), "tmp/user.ser");
    private final UserValidator userValidator = new UserValidator();

    @Override
    public User createUser(String name, String email) {
        if (userValidator.isValidName(name) && userValidator.isValidEmail(email)) {
            User newUser = new User(name, email);
            List<User> saveUserList = loadUserListToFile();
            saveUserList.add(newUser);
            saveUserToFile(saveUserList);
            System.out.println("create user: " + newUser.getId());
            return newUser;
        }
        return null;
    }

    @Override
    public List<User> getAllUserList() {
        return loadUserListToFile();
    }

    @Override
    public User searchById(UUID userId) {
        for (User user : loadUserListToFile()) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        System.out.println("user does not exist");
        return null;
    }

    @Override
    public void updateUserName(UUID userId, String newName) {
        if (!Objects.isNull(searchById(userId)) && userValidator.isValidName(newName)) {
            List<User> saveUserList = loadUserListToFile();
            for (User user:saveUserList) {
                if (user.getId().equals(userId)){
                    user.updateName(newName);
                }
            }
            saveUserToFile(saveUserList);
        }
    }

    @Override
    public void updateUserEmail(UUID userId, String newEmail) {
        if (!Objects.isNull(searchById(userId)) && userValidator.isValidEmail(newEmail)) {
            List<User> saveUserList = loadUserListToFile();
            for (User user:saveUserList) {
                if (user.getId().equals(userId)){
                    user.updateEmail(newEmail);
                }
            }
            saveUserToFile(saveUserList);
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        if (!Objects.isNull(searchById(userId))) {
            List<User> saveUserList = new ArrayList<>();
            for (User user : loadUserListToFile()) {
                if(!user.getId().equals(userId)) {
                    saveUserList.add(user);
                }
            }
            saveUserToFile(saveUserList);
        }
    }

    private void saveUserToFile(List<User> saveUserList) {
        try (
            FileOutputStream fos = new FileOutputStream(filePath.toFile(),false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            for (User user : saveUserList) {
                oos.writeObject(user);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> loadUserListToFile() {
        List<User> data = new ArrayList<>();
        if(!Files.exists(filePath)) {
            return data;
        }
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
            ) {
                while (true) {
                    data.add((User) ois.readObject());
                }
        } catch (EOFException e) {
//            System.out.println("read all objects");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
