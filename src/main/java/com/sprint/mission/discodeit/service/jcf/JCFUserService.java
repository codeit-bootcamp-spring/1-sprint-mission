package com.sprint.mission.discodeit.service.jcf;


import java.util.ArrayList;
import java.util.UUID;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;


public class JCFUserService implements UserService {
    private final ArrayList<User> data;

    public JCFUserService() {
        data = new ArrayList<>();
    }

    // 유저 생성
    public User createUser(String username, String password, String email) {
        UUID uuid = UUID.randomUUID();
        long timestamp = System.currentTimeMillis();
        User user = new User(uuid, username, password, email, timestamp);
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
    public User findUserById(UUID id) {
        for (User user : data) {
            if (user.getId().equals(id)) {
                System.out.println("User found");
                return user;
            }
        }
        System.out.println("User not found");
        return null;
    }
    public ArrayList<User> findAllUsers() {
        return data;
    }

    // 유저 print
    public void printUser(User user) {
        System.out.println(user);
    }
    public void printListUsers(ArrayList<User> users) {
        for (User user : users) {
            System.out.println(user);
        }
    }

    // 유저 삭제
    public void deleteUserById(User user) {
        System.out.println(user.getUsername() + " User deleted");
        data.remove(user);
    }

}
