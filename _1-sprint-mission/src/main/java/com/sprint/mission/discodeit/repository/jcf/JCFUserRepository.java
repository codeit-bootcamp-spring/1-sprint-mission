package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserRepository implements UserRepository {
    // 데이터 저장 HashMap
    private final Map<UUID, User> userData;

    // 생성자
    public JCFUserRepository() {
        this.userData = new HashMap<>();
    }
    //생성 및 업데이트
    @Override
    public User save(User user){
        userData.put(user.getId(),user);
        return user;
    }
    // 단일 조회
    @Override
    public Optional<User> findById(UUID userId){
        return Optional.ofNullable(this.userData.get(userId));
    }
    @Override
    public Optional<User> findByUsername(String username){
        return findAll().stream().filter(u -> u.getUsername().equals(username)).findFirst();
    }
    @Override
    public Optional<User> findByEmail(String email){
        return findAll().stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    // 다중 조회
    @Override
    public List<User> findAll(){
        return new ArrayList<>(userData.values());
    }

    //존재하는지 확인
    @Override
    public boolean existsById(UUID userId) {
        return userData.containsKey(userId);
    }
    @Override
    public boolean existsByEmail(String email) {
        return this.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.findAll().stream().anyMatch(user -> user.getUsername().equals(username));
    }
    //삭제
    @Override
    public void deleteById(UUID userId){
        userData.remove(userId);
    }
}
