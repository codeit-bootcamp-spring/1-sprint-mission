package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    // 데이터 저장 HashMap
    private final Map<UUID, User> userData;

    // 생성자
    public JCFUserRepository() {
        this.userData = new HashMap<>();
    }
    //생성 및 업데이트
    @Override
    public User create(User user){
        userData.put(user.getId(),user);
        return user;
    }
    // 단일 조회
    @Override
    public User findById(UUID userId){
        return userData.get(userId);
    }
    // 다중 조회
    @Override
    public List<User> findAll(){
        return new ArrayList<>(userData.values());
    }
    //삭제
    @Override
    public void delete(UUID userId){
        if(!userData.containsKey(userId)){
            throw new NoSuchElementException("UserId not found");
        }
        userData.remove(userId);
    }
}
