package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID,User> data; //검색 성능을 위해서 map으로 변경
    private static volatile JCFUserService instance;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    //싱글톤
    public static JCFUserService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFUserService();
                }
            }
        }
        return instance;
    }

    @Override
    public void createUser(User user) {
        data.put(user.getId(),user);
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        User userNullable=this.data.get(id);
        return Optional.ofNullable(Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not fount")));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, User updateUser) {
        User existingUser = data.get(id);
        if (existingUser != null) {
            existingUser.update(updateUser.getName(), updateUser.getEmail(), updateUser.getPassword());
        }
    }

    @Override
    public void deleteUser(UUID id) {
        if(!this.data.containsKey(id)){
            throw new NoSuchElementException("User with id " + id + " not fount");
        }
        data.remove(id);
    }
}
