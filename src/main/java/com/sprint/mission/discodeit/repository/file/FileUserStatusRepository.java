package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository, FileService<UserStatus> {
    private static final String USER_STATUS_SAVE_FILE = "config/user-status.ser";
    

    @Override
    public Map<UUID, UserStatus> loadFromFile() {
        File file = new File(USER_STATUS_SAVE_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object data = ois.readObject();

            if (data instanceof Map) {
                return (Map<UUID, UserStatus>) data;
            } else if (data instanceof List) {
                List<UserStatus> userStatuses = (List<UserStatus>) data;
                System.out.println("=======");
                System.out.println(data);
                Map<UUID, UserStatus> userStatusMap = new HashMap<>();
                for (UserStatus userStatus : userStatuses) {
                    userStatusMap.put(userStatus.getUserid(), userStatus);
                }
                return userStatusMap;
            } else {
                throw new IllegalStateException("Unknown data format in file");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public boolean saveToFile(Map<UUID, UserStatus> data) {
        try (FileOutputStream fos = new FileOutputStream(USER_STATUS_SAVE_FILE);

             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(data);
            return true;
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean save(UserStatus userStatus) {
        Map<UUID, UserStatus> userStatuses = loadFromFile();
        userStatuses.put(userStatus.getUserid(), userStatus);
        saveToFile(userStatuses);
        return true;
    }

    @Override
    public UserStatus findByUserId(UUID id) {
        Map<UUID, UserStatus> userStatuses = loadFromFile();

        System.out.println(userStatuses.get(1) );
//        System.out.println(id);
//        System.out.println(userStatuses);
        return loadFromFile().get(id);
    }

}
