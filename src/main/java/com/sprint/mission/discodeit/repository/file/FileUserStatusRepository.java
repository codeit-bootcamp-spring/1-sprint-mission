package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "sprint-mission.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {
    private static final String FILE_PATH = "files/user_statuses.ser";
    private Map<UUID, UserStatus> userStatuses;

    public FileUserStatusRepository() {
        this.userStatuses = loadFromFile();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatuses.put(userStatus.getId(), userStatus);
        saveToFile();
        return userStatus;
    }

    @Override
    public UserStatus findById(UUID id) {
        return userStatuses.get(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatuses.values());
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        for (UserStatus status : userStatuses.values()) {
            if (status.getUserId().equals(userId)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public boolean existsById(UUID id) {
        return userStatuses.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        userStatuses.remove(id);
        saveToFile();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        List<UUID> toDelete = new ArrayList<>();
        for (Map.Entry<UUID, UserStatus> entry : userStatuses.entrySet()) {
            if (entry.getValue().getUserId().equals(userId)) {
                toDelete.add(entry.getKey());
            }
        }
        for (UUID id : toDelete) {
            userStatuses.remove(id);
        }
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(userStatuses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<UUID, UserStatus> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, UserStatus>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}