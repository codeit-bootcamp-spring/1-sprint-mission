package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JcfUserRepository;
import com.sprint.mission.discodeit.service.NetworkClient;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

import static com.sprint.mission.discodeit.util.MyLogger.log;

public class JcfUserService implements UserService {
    private final UserRepository userRepository = new JcfUserRepository();

    @Override
    public void createUser(User user) {
        if (user.getUserName().trim().isEmpty()) {
            log("이름을 입력해주세요.");
            return;
        }
        if (user.getUserEmail().trim().isEmpty()) {
            log("이메일을 입력해주세요.");
            return;
        }
        userRepository.createUser(user.getId(), user);
    }

    @Override
    public Optional<User> findUser(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUserName(UUID id, String userName) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.updateUserName(id, userName);
        } else {
            throw new IllegalArgumentException("해당 아이디를 찾을 수 없습니다");
        }
    }

    @Override
    public void updateUserEmail(UUID id, String userEmail) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.updateUserEmail(id, userEmail);
        } else {
            throw new IllegalArgumentException("해당 아이디를 찾을 수 없습니다");
        }
    }

    @Override
    public void deleteUser(UUID id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteUser(id);
        } else {
            throw new IllegalArgumentException("해당 아이디를 찾을 수 없습니다");
        }
    }


}
