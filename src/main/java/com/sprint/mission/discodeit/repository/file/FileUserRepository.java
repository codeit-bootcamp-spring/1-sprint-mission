package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileUserRepository implements UserRepository, Serializable {


    private static final Long serialVersionUID = 1L;
    private final String fileName = "savedata/user.ser";
    private final Map<UUID, User> userList;

    public FileUserRepository() {
        this.userList = load();
    }
    @Override
    public User save(User user) {
        File file = new File(fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream
                (new FileOutputStream(fileName))) {
            oos.writeObject(userList);
            return user;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패 : " + e.getMessage(), e);
        }
    }

    @Override
    public Map<UUID, User> load() {
        File file = new File(fileName);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream
                (new FileInputStream(file)))
        {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드 실패 : " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(UUID id) {
        userList.remove(id);
    }
}
