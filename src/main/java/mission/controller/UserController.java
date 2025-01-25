package mission.controller;

import mission.entity.User;

import java.util.Set;
import java.util.UUID;

public interface UserController {


    void create(String name, String password);
    void updateUserNamePW(UUID id, String newName, String password);

    User findById(UUID id);
    Set<User> findAll();
    Set<User> findUsersByName(String findName);
    User findUserByNamePW(String name, String password);
    Set<User> findUsersInChannel(UUID channelId);

    void deleteUser(UUID id, String nickName, String password);
    void drops(UUID channel_Id, UUID droppingUser_Id);
    void dropsAllByUser(UUID droppingUser_Id);
    void addChannelByUser(UUID channelId, UUID userId);
}
