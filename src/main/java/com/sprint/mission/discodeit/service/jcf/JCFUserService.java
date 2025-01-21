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
        // 비밀번호 길이 검사
        if (user.getPassword().length() < 10) {
            throw new IllegalArgumentException("비밀번호는 10자리 이상이어야 합니다.");
        }

        // 유저 이름 중복 검사
        boolean isDuplicateUsername = data.values().stream()
                .anyMatch(existingUser -> existingUser.getUsername().equals(user.getUsername()));
        if (isDuplicateUsername) {
            throw new IllegalArgumentException("사용자 이름이 이미 존재합니다: " + user.getUsername());
        }
        data.put(user.getId(), user);
    }

    @Override
    public User getUser(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, String newUsername, String newPassword) {
        User user = data.get(id);
        if (user == null) {
            throw new NoSuchElementException("존재하지 않는 사용자 ID: " + id);
        }

        // 비밀번호 길이 검사
        if (newPassword.length() < 10) {
            throw new IllegalArgumentException("비밀번호는 10자리 이상이어야 합니다.");
        }

        // 새 유저 이름 중복 검사
        boolean isDuplicateUsername = data.values().stream()
                .anyMatch(existingUser -> existingUser.getUsername().equals(newUsername) && !existingUser.getId().equals(id));
        if (isDuplicateUsername) {
            throw new IllegalArgumentException("사용자 이름이 이미 존재합니다: " + newUsername);
        }

        user.updateUsername(newUsername);
        user.updatePassword(newPassword);
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }
}
