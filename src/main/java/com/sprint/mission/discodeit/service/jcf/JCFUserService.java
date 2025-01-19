package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final List<User> users = new ArrayList<>();

    @Override
    public User createUser(String username, String password) {
        User user = new User(username, password);
        users.add(user);
        return user;
    }

    @Override
    public User getUserById(UUID userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }


    @Override
    public List<User> getAllUsers() {
        return users;
    }

    // ======================================================================================================
    // 질문 1에 대한 사항.
    //단건 출력 메서드
    public void printSingleUser(UUID userId) {
        User user = getUserById(userId);
        if (user != null) {
            System.out.println(user.getUsername() + "'s UUID : " + user.getId() +
                    ", Password : " + user.getPassword() +
                    ", CreatedAt : " + user.getCreatedAt() +
                    ", UpdatedAt : " + user.getUpdatedAt());
        } else {
            System.out.println("해당 ID의 유저를 찾을 수 없습니다.");
        }
    }

    //다건 출력 메서드
    public void printAllUsers() {
        List<User> users = getAllUsers();
        if (!users.isEmpty()) {
            for (User user : users) {
                System.out.println(user.getUsername() + "'s UUID : " + user.getId() +
                        ", Password : " + user.getPassword() +
                        ", CreatedAt : " + user.getCreatedAt() +
                        ", UpdatedAt : " + user.getUpdatedAt());
            }
        } else {
            System.out.println("현재 등록된 유저가 없습니다.");
        }
    }
    // ======================================================================================================

    @Override
    public User updateUsername(UUID userId, String newUsername) {
        User user = getUserById(userId);
        if (user != null) {
            user.updateUsername(newUsername);
        }
        return user;
    }

    @Override
    public User updatePassword(UUID userId, String newPassword) {
        User user = getUserById(userId);
        if (user != null) {
            user.updatePassword(newPassword);
        }
        return user;
    }

    @Override
    public boolean deleteUser(UUID userId) {//삭제의 경우 읽기 전용인 향상된 for문을 사용하면 오류 = 일반 for문을 사용하였다.
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(userId)) {
                users.remove(i);
                return true;
            }
        }
        return false;
    }
}
