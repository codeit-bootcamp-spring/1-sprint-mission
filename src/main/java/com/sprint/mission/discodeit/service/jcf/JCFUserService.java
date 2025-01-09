package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> data;

    public JCFUserService() {
        this.data = new ArrayList<>();
    }

    @Override
    public void createUser(User user) {
        data.add(user);
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return data.stream()
                .filter(user->user.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data);
    }

    @Override
    public void updateUser(UUID id, User updateUser) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(id)) {
                // 기존 객체를 수정
                User existingUser = data.get(i);
                existingUser.update(updateUser.getName(), updateUser.getPassword());
                break;
            }
        }
    }

    @Override
    public void deleteUser(UUID id) {
        data.removeIf(user->user.getId().equals(id));
    }
}
