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

    // Create User
    @Override
    public User createUser(String name, String email) {
        if (correctName(name) && correctEmail(email)) {
            User newUser = new User(name, email);
            data.add(newUser);
            System.out.println("환영합니다! " + newUser.getName() + "님 반갑습니다.");
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
        System.out.println("해당하는 사용자가 없습니다.");
        return null;
    }

    // 유저 삭제
    @Override
    public void deleteUser(User deleteUser) {
        data.remove(deleteUser);
        System.out.println("탈퇴되었습니다.");
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
        if (correctName(newName)) {
            user.updateName(newName);
            System.out.println("수정되었습니다.");
        }
    }

    // 유저 이메일 업데이트
    @Override
    public void updateUserEmail(User user, String newEmail) {
        if (correctEmail(newEmail)) {
            user.updateEmail(newEmail);
            System.out.println("수정되었습니다.");
        }
    }

    private boolean correctName(String name) {
        if (name.isBlank()) {
            System.out.println("이름을 입력해주세요");
        } else if (name.length() < 2) {
            System.out.println("이름은 두글자 이상 입력해주세요");
        } else {
            return true;
        }
        return false;
    }

    private boolean correctEmail(String email) {
        String emailFormat = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailFormat)) {
            System.out.println("이메일 형식이 올바르지 않습니다.");
        }
        return email.matches(emailFormat);
    }
}