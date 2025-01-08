package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BaseService;

import java.util.*;

public class JCFUserService implements BaseService<User> {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User create(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User readById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, User user) {
        try{
            User checkUser = data.get(id);
            if (checkUser != null) {
                checkUser.update(user.getNickname(), user.getUsername(), user.getEmail(), user.getPhoneNumber());
            }
            return checkUser;
        } catch (IllegalArgumentException e){
            System.out.println("유효하지 않는 id입니다.");
            return null;
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
