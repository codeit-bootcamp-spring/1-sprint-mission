package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JcfMessageService;
import com.sprint.mission.discodeit.service.jcf.JcfUserService;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserRepository implements UserRepository {
    public final Map<UUID,User> map;
    //private JcfMessageService jcfMessageService; //수정 예정
    private static volatile JCFUserRepository instance;

    public JCFUserRepository(Map<UUID, User> list) {
        this.map = list;
    }


    @Override
    public UUID save(String userName) {
        User user = new User(userName);
        map.put(user.getUserId(),user);
        return user.getUserId();
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
    public void update(UUID id, String username) {
        if(map.containsKey(id)){
            User user = findUserById(id);
            user.update(username);
        }else {
            System.out.println("유저를 찾을 수 없습니다.");
        }
    }
}
