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
    public void CreateUserDefault(String name) {
        userService.CreateUserDefault(name);
    }

    @Override
    public <T> String ReadUser(T user) {
        return userService.ReadUser(user);
    }

    @Override
    public String ReadUserAll() {
        return userService.ReadUserAll();
    }

    @Override
    public boolean UpdateUserName(String name, String change) {
        return userService.UpdateUserName(name,change);
    }

    @Override
    public boolean UpdateUserName(UUID id, String changeName) {
        return userService.UpdateUserName(id,changeName);
    }

    @Override
    public boolean DeleteUser(UUID id) {
        return userService.DeleteUser(id);
    }

    @Override
    public boolean DeleteUser(String name) {
        return userService.DeleteUser(name);
    }
}
