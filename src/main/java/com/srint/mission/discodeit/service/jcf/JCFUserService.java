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
        if(!data.containsKey(user.getId())){
            data.put(user.getId(), user);
        }
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

    public UUID delete(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("삭제할 User를 찾지 못했습니다.");
        }
        data.remove(id);
        return id;
    }


    //서비스 로직
    @Override
    public UUID create(String username, String email, String password) {
        validateDuplicateUser(email);
        User user = new User(username, email, password);
        return save(user);
    }

    @Override
    public User read(UUID id) {
        return findOne(id);
    }

    @Override
    public List<User> readAll() {
        return findAll();
    }

    @Override
    public User updateUserName(UUID id, String name) {
        User findUser = findOne(id);
        findUser.setUsername(name);
        save(findUser);
        return findUser;
    }

    @Override
    public User updateEmail(UUID id, String email) {
        User findUser = findOne(id);
        findUser.setEmail(email);
        save(findUser);
        return findUser;
    }

    @Override
    public User updatePassword(UUID id, String password) {
        User findUser = findOne(id);
        findUser.setPassword(password);
        save(findUser);
        return findUser;
    }

    @Override
    public UUID deleteUser(UUID id) {
        User findUser = findOne(id);
        if(!findUser.getMyChannels().isEmpty()){
            findUser.getMyChannels().forEach(
                    channel -> channel.deleteJoinedUser(findUser));
        }
        return delete(findUser.getId());

    }

    private void validateDuplicateUser(String email) {
        if(!data.isEmpty()){
            List<User> users = findAll();
            if (users.stream().anyMatch(user-> email.equals((user.getEmail())))){
                throw new IllegalStateException("이미 존재하는 이메일 입니다.");
            }
        }

    }
}
