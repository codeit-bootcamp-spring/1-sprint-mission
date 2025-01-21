package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileUserService implements UserService, FileService<User> {
    private static final String USER_PATH = "user.ser";

    private Map<UUID, User> userList = new HashMap<>();

    private static EntityFactory ef;

    public FileUserService(EntityFactory ef) {
        FileUserService.ef = ef;
    }

    public FileUserService() {
        ef = BaseEntityFactory.getInstance();
    }

    @Override
    public User createUser(String userName, String userEmail, String userPassword) {
        User user = ef.createUser(userName, userEmail, userPassword);
        userList.put(user.getUserId(), user);

        save(USER_PATH, userList);

        return user;
    }

    @Override
    public User getUserById(UUID userId) {
        return userList.get(userId);
    }

    @Override
    public Map<UUID, User> getAllUsers() {
        return load(USER_PATH, userList);
    }

    @Override
    public User updateUser(UUID userId, String newName, String newEmail, String newPassword) {
        User findUser = getUserById(userId);

        if (findUser == null) {
            throw new IllegalArgumentException("찾는 유저가 없습니다");
        } else {
            findUser.updateName(newName);
            findUser.updateEmail(newEmail);
            findUser.updatePassword(newPassword);
        }

        save(USER_PATH, userList);


        return findUser;
    }

    @Override
    public void deleteUser(UUID userId) {
        userList.remove(userId);

        save(USER_PATH, userList);

    }

}
