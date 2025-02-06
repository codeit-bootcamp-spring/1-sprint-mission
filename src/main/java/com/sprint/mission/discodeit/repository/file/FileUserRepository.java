package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
    private static FileUserRepository instance;

    public static FileUserRepository getInstance() {
        if (instance == null) {
            instance = new FileUserRepository();
        }
        return instance;
    }

    @Override
    public void save(Map<UUID, User> userList) {
        try (FileOutputStream fos = new FileOutputStream("User.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (User user : userList.values()) {
                oos.writeObject(user);
            }
        } catch (IOException e) {
            System.err.println("Error serializing users: " + e.getMessage());
        }
    }
    @Override
    public Map<UUID, User> load() {
        Map<UUID, User> userList = new HashMap<>();

        try (FileInputStream fis = new FileInputStream("User.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            while (true) {
                try {
                    User user = (User) ois.readObject();
                    userList.put(user.getId(), user);
                } catch (ClassNotFoundException e) {
                    System.err.println("ClassNotFoundException: " + e.getMessage());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

        return userList;
    }

}

