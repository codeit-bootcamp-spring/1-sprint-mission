package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public void addUser(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public User getUser(UUID id) {
        User user = data.get(id);
        if (user == null) {
            throw new NoSuchElementException("해당 ID를 가진 사용자가 존재하지 않습니다: " + id);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, String newUsername, String newPassword) {
        User user = data.get(id);
        if (user != null) {
            user.updateUsername(newUsername);
            user.updatePassword(newPassword);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        User removedUser = data.remove(id);
        if (removedUser != null) {
            System.out.println("삭제된 사용자: " + removedUser.getUsername() + " (ID: " + removedUser.getId() + ")");
        } else {
            System.out.println("삭제 실패: 해당 ID를 가진 사용자가 존재하지 않습니다. ID: " + id);
        }
    }
}
