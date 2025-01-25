package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "tmp/users.ser";
    private final List<User> userList;

    public FileUserRepository() {
        this.userList = loadFromFile();
    }

    @Override
    public void save(User user) {
        if(userList.stream().anyMatch(u -> u.getUserId().equals(user.getUserId()))){
            throw new IllegalArgumentException("Duplicate User ID: " + user.getUserId());
        }
        userList.add(user);
        saveToFile();
    }

    @Override
    public User findById(String userId) {
        User findUser = loadFromFile().stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
        if(findUser == null){
            throw new IllegalArgumentException("User with ID " +userId + " not found.");
        }
        return findUser;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(loadFromFile());
    }

    @Override
    public void delete(String userId) {
        userList.removeIf(user -> user.getUserId().equals(userId));
        saveToFile();
    }



    private List<User> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Initializing empty list.");
            return new ArrayList<>();
        }catch (EOFException e){
            System.out.println("EOFException: File is empty or corrupted. Initializing empty list.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos
                     = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(userList);
        } catch (IOException e) {

            throw new RuntimeException("Failed to save users to file.", e);
        }

    }

}
