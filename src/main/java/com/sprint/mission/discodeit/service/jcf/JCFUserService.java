package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;
    public JCFUserService(){
        this.data = new HashMap<>();
    }

    @Override
    public User create(User user){
        if (data.containsKey(user.getId())) {
            throw new IllegalArgumentException("User ID already exists: " + user.getId());
        }
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> read(User user) {
        return Optional.ofNullable(data.get(user.getId()));
    }

    @Override
    public List<User> readAll(){
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(User existUser, User updateUser){
        if (!data.containsKey(existUser.getId())) {
            throw new NoSuchElementException("User not found");
        }
        // 기존 객체의 값을 업데이트
        existUser.updateTime();
        data.put(existUser.getId(), updateUser);
        return updateUser;
    }

    @Override
    public boolean delete(User user){
        if (!data.containsKey(user.getId())) {
            return false;
        }
        data.remove(user.getId());
        return true;
    }
}
