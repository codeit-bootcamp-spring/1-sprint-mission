package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.file.FileUserService;
import com.sprint.mission.discodeit.repository.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class BasicUserService implements UserService {

    private UserService userService;

    public BasicUserService() {
        this.userService = new FileUserService();
    }

    public void SetUpJfcUser(){
        this.userService=new JCFUserService();
    }
    public void SetUpFileUser(){
        this.userService=new FileUserService();
    }


    @Override
    public void createNewUser(String name) {
        userService.createNewUser(name);
    }

    @Override
    public <T> String readUser(T user) {
        return userService.readUser(user);
    }

    @Override
    public String readUserAll() {
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
    public boolean deleteUser(UUID id) {
        return userService.deleteUser(id);
    }

    @Override
    public boolean deleteUser(String name) {
        return userService.deleteUser(name);
    }
}
