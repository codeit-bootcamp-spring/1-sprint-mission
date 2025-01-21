package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.UUID;

public class UserService implements JCFUserRepository{
    private final HashMap<UUID, User> data = new HashMap<>();

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
        System.out.println("유저 저장 완료 : " + user.getId());
    }

    @Override
    public void findById(UUID id) {
        if(data.containsKey(id)) {
            System.out.println("userEmail = " + data.get(id).getUserEmail());
            System.out.println("userNickName = " + data.get(id).getUserNickName());
            System.out.println("password = " + data.get(id).getPassword());
        }
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
    public void update(User user) {
        // 이 함수가 호출되면 해당 엔티티의 updateAt값을 변경 시점 시간으로 저장해주어야 한다.
        data.put(user.getId(), user);
        System.out.println("유저 정보 변경 완료");
        user.updateUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
