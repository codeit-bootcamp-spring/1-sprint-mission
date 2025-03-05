package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final String filePath = "user_status.dat";

    @Override
    public UserStatus save(UserStatus userStatus) {
        Map<String, UserStatus> userStatusMap = readFromFile();
        userStatusMap.put(userStatus.getUserId(), userStatus);
        writeToFile(userStatusMap);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findByUserId(String userId) {
        return Optional.ofNullable(readFromFile().get(userId));
    }

    @Override
    public void deleteByUserId(String userId) {
        Map<String, UserStatus> userStatusMap = readFromFile();
        userStatusMap.remove(userId);
        writeToFile(userStatusMap);
    }

    private Map<String, UserStatus> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<String, UserStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void writeToFile(Map<String, UserStatus> userStatusMap) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(userStatusMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}