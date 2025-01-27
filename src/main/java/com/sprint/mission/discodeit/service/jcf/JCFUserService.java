package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> userRepository;

    public JCFUserService() {
        this.userRepository = new HashMap<>();
    }

    @Override
    public User create(String name, String phone, String password) {
        if (!User.isValidPassword(password)) {
            throw new ServiceException(ErrorCode.INVALID_PASSWORD);
        }

        if (!User.isValidPhone(phone)) {
            throw new ServiceException(ErrorCode.INVALID_WRITER);
        }

        if (userRepository.values().stream()
                .anyMatch(user -> user.getPhone().equals(phone))) {
            throw new ServiceException(ErrorCode.CANNOT_FOUND_USER);
        }
        User user = new User(name, phone, password);
        userRepository.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUserByPhone(String phone) {
        return userRepository.values().stream()
                .filter(user -> user.getPhone().equals(phone))
                .findFirst();
    }

    @Override
    public List<User> getAllUser() {
        return new ArrayList<>(userRepository.values());
    }

    @Override
    public User updateUserPassword(User updateUser, String newPass) {
        if (!userExists(updateUser.getPhone())) {
            throw new ServiceException(ErrorCode.CANNOT_FOUND_USER);
        }
        User findUser = userRepository.get(updateUser.getId());
        findUser.update(newPass);
        return findUser;
    }

    @Override
    public void delete(User removeUser) { // 유저 정보 삭제 시 유저가 속해있던 채널에 해당 유저가 삭제되어야 한다.
        if (!userExists(removeUser.getPhone())) {
            throw new ServiceException(ErrorCode.CANNOT_FOUND_USER);
        }
        userRepository.remove(removeUser.getId());
    }

    private boolean userExists(String phone) {
        return userRepository.values()
                .stream()
                .anyMatch(user -> user.getPhone().equals(phone));
    }
}
