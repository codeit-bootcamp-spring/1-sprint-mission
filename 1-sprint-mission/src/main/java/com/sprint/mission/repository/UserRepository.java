package com.sprint.mission.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.service.jcf.EntityService;

import java.text.SimpleDateFormat;
import java.util.*;

public class UserRepository {
    private static final UserRepository INSTANCE = new UserRepository();
    private UserRepository() {}

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    private final List<User> users = new ArrayList<>();

    public User createUser(String nickname) {
        User user = new User();
        user.setName(nickname);
        users.add(user);
        return user;
    }

    public void update(UUID id, String newNickname) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                user.setName(newNickname);
                user.setUpdatedAt(System.currentTimeMillis());
            }
        }
    }

    public boolean checkUserId(UUID id){
        for(User user : users){
            if(user.getId().equals(id)){
                return true;
            }
        }

        return false;
    }

    public void deleteUser(UUID id) {
        for(User user : users){
            if(user.getId().equals(id)){
                users.remove(user);
                break;
            }
        }
    }
}
