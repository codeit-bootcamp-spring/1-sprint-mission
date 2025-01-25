package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 생성
    public User createUser(String username, String password, String email) {
        User user = new User(username, password, email);
        if (userRepository.saveUser(user)) {
            System.out.println("Created user " + username);
            return user;
        }
        return null;
    }

    // 정보 수정
    public User updateUserName(User user, String username) {
        User updateUser = userRepository.loadUser(user);
        if (updateUser != null) {
            updateUser.updateName(username);
            if (userRepository.saveUser(updateUser)) {
                System.out.println("User name updated");
                return updateUser;
            }
        }
        return null;
    }

    public User updatePassword(User user, String password) {
        User updateUser = userRepository.loadUser(user);
        if (updateUser != null) {
            updateUser.updatePassword(password);
            if (userRepository.saveUser(updateUser)) {
                System.out.println("User password updated");
                return updateUser;
            }
        }
        return null;
    }

    public User updateEmail(User user, String email) {
        User updateUser = userRepository.loadUser(user);
        if (updateUser != null) {
            updateUser.updateEmail(email);
            if (userRepository.saveUser(updateUser)) {
                System.out.println("User email updated");
                return updateUser;
            }
        }
        return null;
    }

    // 조회
    public User findUserById(User u) {
        return userRepository.loadUser(u);
    }
    public List<User> findAllUsers() {
        return userRepository.loadAllUser();
    }

    // 유저 프린트
    public void printUser(User user) {
        System.out.println(user);
    }
    public void printListUsers(List<User> users) {
        for (User user : users) {
            System.out.println(user);
        }
    }

    // 삭제
    public void deleteUserById(User user) {
        if (userRepository.deleteUser(user)) {
            System.out.println(user.getUsername() + " User deleted");
        }
    }
}
