package com.sprint.mission.discodeit.service.jcf;


import java.util.List;
import java.util.ArrayList;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;


public class JCFUserService implements UserService {
    private final List<User> data;

    public JCFUserService() {
        data = new ArrayList<>();
    }

    // 유저 생성
    public User createUser(String username, String password, String email) {
        User user = new User(username, password, email);
        data.add(user);
        System.out.println("Created user " + username);
        return user;
    }

    // 유저 정보 수정
    public User updateUserName(User user, String username) {
        user.updateName(username);
        System.out.println("User name updated");
        return user;
    }
    public User updatePassword(User user, String password) {
        user.updatePassword(password);
        System.out.println("User password updated");
        return user;
    }
    public User updateEmail(User user, String email) {
        user.updateEmail(email);
        System.out.println("User email updated");
        return user;
    }

    // 유저 조회
    public User findUserById(User u) {
        for (User user : data) {
            if (user.getId().equals(u.getId())) {
                System.out.println("User found");
                return user;
            }
        }
        System.out.println("User not found");
        return null;
    }
    public List<User> findAllUsers() {
        return data;
    }

    // 유저 print
    public void printUser(User user) {
        System.out.println(user);
    }
    public void printListUsers(List<User> users) {
        users.forEach(System.out::println);
    }

    // 유저 삭제
    public void deleteUserById(User user) {
        System.out.println(user.getUsername() + " User deleted");
        data.remove(user);
    }

}
