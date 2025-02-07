package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
@Profile("Jcf")
public class JCFUserRepository implements UserRepository {
    public final Map<UUID,User> map;

    public JCFUserRepository() {
        this.map = new HashMap<>();
    }

    @Override
    public User save(String userName,String password, String email) {
        User user = new User(userName,password, email);
        map.put(user.getId(),user);
        return user;
    }

    @Override
    public User findUserById(UUID id) {
        return map.get(id);
    }

    @Override
    public List<User> findAll() {
        List<User> collect = map.values().stream().toList();
        return new ArrayList<>(collect);
    }

    @Override
    public boolean delete(UUID id) {
        if(map.containsKey(id)){
            map.remove(id);
            return true;
        }else {
            System.out.println("유저를 찾을 수 없습니다.");
            return false;
        }
    }

    @Override
    public void update(UUID id, String username, String email) {
        if(map.containsKey(id)){
            User user = findUserById(id);
            user.update(username, email);
            map.replace(id, user);
        }else {
            System.out.println("유저를 찾을 수 없습니다.");
        }
    }
}
