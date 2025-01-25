package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    // 생성자에서 UserRepository를 주입
    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(User user) {
        userRepository.createUser(user); // Repository에 저장
    }

    // Basic*Service 구현체를 활용
    @Override
    public User createUser(String userName, String email, String password) {
        User user = new User(UUID.randomUUID(), System.currentTimeMillis(), System.currentTimeMillis(), userName, email, password);
        userRepository.createUser(user); // Repository에 저장
        return user;
    }

    @Override
    public User getUser(UUID id) {
        return userRepository.getUser(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void updateUser(UUID id, String userName) {
        User user = getUser(id);
        user.update(userName);  // 비즈니스 로직
        userRepository.updateUser(id, userName);  // Repository에 반영
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteUser(id);
    }
}
