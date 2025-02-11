package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("BasicUserService")
public class BasicUserService implements UserService {

    private final UserService userService;


    public BasicUserService(@Qualifier("FileUserService") UserService userService) {
        this.userService = userService;
    }


    @Override
    public void createNewUser(String name,String password,String email) {
        userService.createNewUser(name,password,email);
    }

    @Override
    public <T> List<User> readUser(T user) {
        return userService.readUser(user);
    }

    @Override
    public List<User> readUserAll() {
        return userService.readUserAll();
    }

    @Override
    public boolean updateUserName(String name, String change) {
        return userService.updateUserName(name,change);
    }

    @Override
    public boolean updateUserName(UUID id, String changeName) {
        return userService.updateUserName(id,changeName);
    }

    @Override
    public boolean deleteUser(UUID id,String password) {
        return userService.deleteUser(id,password);
    }

    @Override
    public boolean deleteUser(String name,String password) {
        return userService.deleteUser(name,password);
    }

    @Override
    public boolean updateUserSelfImg(String name, char[] img) {
        return userService.updateUserSelfImg(name,img);
    }

    @Override
    public boolean updateUserSelfImg(UUID id, char[] img) {
        return userService.updateUserSelfImg(id,img);
    }

    @Override
    public <T, C, K> boolean sendMessageToUser(T sender, C receiver, K message) {
        return userService.sendMessageToUser(sender,receiver,message);
    }


}
