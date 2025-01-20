package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> data;

    public JCFUserService() {
        data = new ArrayList<>();
    }

    // 유저 생성
    @Override
    public User createUser(String name, String email) {
        if (isValidName(name) && isValidEmail(email)) {
            User newUser = new User(name, email);
            data.add(newUser);
            System.out.println("user create: " + newUser.getName());
            return newUser;
        }
        return null;
    }

    // 모든 유저 조회
    @Override
    public List<User> getAllUserList() {
        return data;
    }

    // ID으로 유저찾기
    @Override
    public User searchById(UUID userId) {
        for (User user : data) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        System.out.println("user dose not exist");
        return null;
    }

    // 유저 삭제
    @Override
    public void deleteUser(User deleteUser) {
        data.remove(deleteUser);
        System.out.println("success delete");
    }

    @Override
    public void printUserInfo(User user) {
        System.out.println(user);
    }

    @Override
    public void printUserListInfo(List<User> userList) {
        for (User user : userList) {
            printUserInfo(user);
        }
    }

    // 유저 이름 업데이트
    public void updateUserName(User user, String newName) {
        if (isValidName(newName)) {
            user.updateName(newName);
            System.out.println("success update");
        }
    }

    // 유저 이메일 업데이트
    @Override
    public void updateUserEmail(User user, String newEmail) {
        if (isValidEmail(newEmail)) {
            user.updateEmail(newEmail);
            System.out.println("success update");
        }
    }

    private boolean isValidName(String name) {
        if (name.isBlank()) {
            System.out.println("the name is blank");
        } else if (name.length() < 2) {
            System.out.println("the name is too short");
        } else {
            return true;
        }
        return false;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            System.out.println("email format does not match");
        }
        return email.matches(emailRegex);
    }
}
