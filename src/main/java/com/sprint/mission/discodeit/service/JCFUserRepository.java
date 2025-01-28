package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {
    private final HashMap<UUID, User> data = new HashMap<>();

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
        System.out.println("유저 저장 완료 : " + user.getId());
    }

    @Override
    public String findById(String id) {
        if(data.containsKey(id)) {
            System.out.println("userEmail = " + data.get(id).getUserEmail());
            System.out.println("userNickName = " + data.get(id).getUserNickName());
            System.out.println("password = " + data.get(id).getPassword());
        }
        return id;
    }

    @Override
    public void findAll() {
        for (UUID uuid : data.keySet()) {
            System.out.println("userID = " + uuid);
            System.out.println("userEmail = " + data.get(uuid).getUserEmail());
            System.out.println("userNickName = " + data.get(uuid).getUserNickName());
            System.out.println("password = " + data.get(uuid).getPassword());
        }
    }

    @Override
    public void update(String user) {

    }

    @Override
    public void delete(String id) {

    }
}
