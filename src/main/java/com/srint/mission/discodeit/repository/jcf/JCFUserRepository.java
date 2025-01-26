package com.srint.mission.discodeit.repository.jcf;

import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data;

    public JCFUserRepository() {
        data = new HashMap<>();
    }

    //DB 로직
    public UUID save(User user) {
        data.put(user.getId(), user);
        return user.getId();
    }

    public User findOne(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("조회할 User을 찾지 못했습니다.");
        }
        return data.get(id);
    }

    public List<User> findAll() {
        if(data.isEmpty()){
            return Collections.emptyList(); // 빈 리스트 반환
        }
        return data.values().stream().toList();
    }

    @Override
    public UUID update(User user) {
        data.put(user.getId(), user);
        return user.getId();
    }

    public UUID delete(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("삭제할 User를 찾지 못했습니다.");
        }
        data.remove(id);
        return id;
    }


}
