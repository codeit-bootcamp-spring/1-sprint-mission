package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {
    private final String filePath;
    public FileUserRepository() {
        this.filePath = "tmp/user.ser";
    }
    public FileUserRepository(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public void save(User user) {
        Map<UUID, User> userMap = this.findAll();
        if(userMap == null) {
            userMap = new HashMap<>();
        }
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            userMap.put(user.getId(), user);
            oos.writeObject(userMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public User findById(UUID userId) {
        Map<UUID, User> userMap = this.findAll();
        Optional<User> findUser = userMap.values().stream().filter(user -> user.getId().equals(userId))
                .findAny();
        return findUser.orElseThrow(() -> new NoSuchElementException("userId :" + userId + "를 찾을 수 없습니다."));
    }

    @Override
    public void delete(UUID userId) {
        Map<UUID, User> userMap = this.findAll();
        if(userMap == null || !userMap.containsKey(userId)) {
            throw new NoSuchElementException("UserId :" + userId + "를 찾을 수 없습니다.");
        }
        userMap.remove(userId);
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(userMap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Map<UUID, User> findAll() {
        Map<UUID, User> userMap = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis);) {
            userMap = (Map<UUID, User>) ois.readObject();
        } catch (EOFException e) {
            return userMap;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return userMap;
    }
}