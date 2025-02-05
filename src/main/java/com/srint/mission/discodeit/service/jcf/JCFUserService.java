package com.srint.mission.discodeit.service.jcf;


import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.service.UserService;

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
/*        if (data.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }*/
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
    public User updateUserName(UUID id, String name) {
        User findUser = findOne(id);
        findUser.setUsername(name);
        update(findUser);
        return findUser;
    }

    @Override
    public User updateEmail(UUID id, String email) {
        User findUser = findOne(id);
        findUser.setEmail(email);
        update(findUser);
        return findUser;
    }

    @Override
    public User updatePassword(UUID id, String password) {
        User findUser = findOne(id);
        findUser.setPassword(password);
        update(findUser);
        return findUser;
    }

    @Override
    public UUID deleteUser(UUID id) {
        User findUser = findOne(id);
        return delete(findUser.getId());
    }

}