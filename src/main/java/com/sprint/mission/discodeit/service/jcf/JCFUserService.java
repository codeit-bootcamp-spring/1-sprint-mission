package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> data;

    public JCFUserService(){

        this.data = new ArrayList<>();
    }

    @Override
    public User createUser(UUID id, String userName){
        User user = new User(id, userName);
        data.add(user);
        return user;
    }

    @Override
    public User getUser(UUID id) {
        for (User user : data) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> allUsers(){
        return new ArrayList<>(data);
    }

    @Override
    public void updateUser(UUID id, String newUserName){
        User user = getUser(id);
        if(user != null){
            user.setUserName(newUserName);
            user.setUpdatedAt(System.currentTimeMillis());
        }
    }


    @Override
    public void deleteUser(UUID id){
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).getId().equals(id))
                data.remove(i);
            return;
        }
    }

    public void printAllUsers(){
        for (int i = 0; i < data.size(); i++) {
            User user = data.get(i);
            System.out.println("User " + (i + 1) + ":");
            System.out.println("ID: " + user.getId());
            System.out.println("Name: " + user.getUserName());
            System.out.println("Created At: " + user.getCreateAt());
            System.out.println("Updated At: " + user.getUpdateAt());
            System.out.println("------------------------");
        }
    }




}
