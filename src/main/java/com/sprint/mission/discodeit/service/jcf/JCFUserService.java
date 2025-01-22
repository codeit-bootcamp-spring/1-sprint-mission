package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> userData;

    public JCFUserService() {
        this.userData = new HashMap<>();
    }

    @Override
    public User createUser(String username) {
        User newUser = new User(username);
        userData.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User getUserById(UUID userId) {
        return userData.getOrDefault(userId, null);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userData.values());
    }

    @Override
    // 질문: method signature에 throws new IllegalArgumentException을 쓰는게 좋겠죠?
    public void updateUsername(UUID userId, String newUsername) {
        // 질문: 위 메서드를 사용하는게 좋은지,아니면  data.getOrDefault(id, null); 하는게 좋은지
        User user = getUserById(userId);

        // 질문: try catch?
        if (user != null) {
            //질문: user model의 updateUsername과 현재 메서드의 이름이 같아서 둘중 하나를 바꾸는게 좋은지
            user.updateUsername(newUsername);
        } else {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        userData.remove(userId);
    }
}
