package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(String userName) {
        User user = new User(userName);
        userRepository.save(user);
        return user;
    }

    @Override
    public User readUser(UUID userId) {
        return userRepository.findbyId(userId);
    }

    @Override
    public List<User> readAllUser() {
        List<User> userList = new ArrayList<>(userRepository.load().values());
        return userList;
    }

    @Override
    public User modifyUser(UUID userID, String newName) {
        User user = userRepository.findbyId(userID);
        user.updateUsername(newName);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userID) {
        userRepository.delete(userID);
    }
}
