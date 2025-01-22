package com.srint.mission.discodeit.repository.jcf;

import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data;

    public JCFUserRepository() {
        data = new HashMap<>();
    }


    //DB 로직
    @Override
    public UUID save(User user) {
        data.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public User findOne(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("조회할 User을 찾지 못했습니다.");
        }
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        if(data.isEmpty()){
            throw new IllegalArgumentException("User가 없습니다.");
        }
        return data.values().stream().toList();
    }

    @Override
    public UUID delete(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("삭제할 User를 찾지 못했습니다.");
        }
        data.remove(id);
        return id;
    }
}
