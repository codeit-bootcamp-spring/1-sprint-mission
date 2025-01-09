package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JcfUserService implements UserService {
    private List<User> data=new ArrayList<>();

    @Override
    public void create(User user) {
        data.add(user);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data);
    }

    @Override
    public void update(UUID id, User updatedUser) {
        for (User user : data) {
            if(user.getId().equals(id)) {
                user.updateUsername(updatedUser.getUsername());
                user.updateEmail(updatedUser.getEmail());
                user.getUpdatedAt();
                return;
            }
        }
    }

    @Override
    public void delete(UUID id) {
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).getId().equals(id)){
                data.remove(i);
                return;
        }
        }
    }
}
