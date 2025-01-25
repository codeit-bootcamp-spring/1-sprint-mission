package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class JCFUserRepository implements UserRepository {
    private final List<User> userList = new ArrayList<>();


    @Override
    public void save(User user) {
        if(user == null || user.getUserId() == null){
            throw new IllegalArgumentException(" User or User ID cannot be null. ");
        }
        userList.add(user);
        System.out.println("<<< User saved successfully >>>");
    }

    @Override
    public User findById(String userId) {
        if(userId == null || userId.isEmpty()){
            throw new IllegalArgumentException(" User ID cannot be null or empty. ");
        }
        return userList.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User with ID " +userId + " not found."));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userList);
    }

    @Override
    public void delete(String userId) {
        boolean removed = userList.removeIf(channel ->
                channel.getUserId().equals(userId));

        if (removed) {
            System.out.println("User ID with UUID " + userId + " was deleted.");
        } else {
            System.out.println("User ID with UUID " + userId + " not found.");
        }
    }



}
