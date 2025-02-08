package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data;

    public JCFUserService() {
        data = new HashMap<>();
    }


    //DB 로직
    public UUID save(User user) {
        data.put(user.getId(), user);
        return user.getId();
    }

    public User findOne(UUID id) {
        return data.get(id);
    }

    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    public UUID update(User user){
        data.put(user.getId(), user);
        return user.getId();
    }

    public UUID delete(UUID id) {
        data.remove(id);
        return id;
    }


    //서비스 로직
    @Override
    public UUID create(String username, String email, String password) {
        if(!User.validation(username, email, password)){
            throw new IllegalArgumentException("잘못된 형식입니다.");
        }
        User user = new User(username, email, password);
        return save(user);
    }

    @Override
    public User read(UUID id) {
        User findUser = findOne(id);
        return Optional.ofNullable(findUser)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 없습니다."));
    }

    @Override
    public List<User> readAll() {
        return findAll();
    }


    @Override
    public User updateUser(UUID id, String name, String email){
        User findUser = findOne(id);
        findUser.setUser(name, email);
        update(findUser);
        return findUser;
    }

    @Override
    public UUID deleteUser(UUID id) {
        User findUser = findOne(id);
        return delete(findUser.getId());
    }

}