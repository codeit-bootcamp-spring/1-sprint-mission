package com.sprint.mission.jcf;

import com.sprint.mission.entity.User;
import com.sprint.mission.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final List<User> userData = new ArrayList<>();

    // 계정 생성, 이메일 형식이 아닐 경우 예외 처리
    @Override
    public User createUser(String email) {
        if (email.contains("@") && email.contains(".")) {
            User newUser = new User(UUID.randomUUID(), email);
            userData.add(newUser);
            System.out.println("계정 생성 : " + newUser + "\n계정이 성공적으로 생성되었습니다!\n");
            return newUser;
        } else {
            throw new IllegalArgumentException("올바른 이메일 형식인지 확인해 주세요.");
        }
    }


    // 이메일 변경, 이메일 형식이 아닐 경우 예외 처리
    @Override
    public void updateMail(User user, String mail) {
        if (mail.contains("@") && mail.contains(".") &&! user.getEmail().equals(mail)){
            user.setEmail(mail);
            System.out.println("이메일 변경 : " + user + "\n이메일이 변경되었습니다.\n");
        } else {
            throw new IllegalArgumentException("올바른 이메일 형식인지 확인해 주세요.");
        }
    }

    // 모든 유저 조회
    @Override
    public List<User> getSearchAllUser() {
        System.out.println("유저 목록 = " + userData);
        return new ArrayList<>(userData);
    }

    // 특정 유저 조회
    @Override
    public void searchUser(User user) {
        System.out.println(user);
    }


    // 계정 삭제, 유저를 찾을 수 없을 경우 예외 처리
    @Override
    public void deleteUser(User user) {
        if (userData.contains(user)) {
            userData.remove(user);
            System.out.println("계정 삭제 : " + user + "\n계정이 성공적으로 삭제되었습니다.\n");
        } else {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다");
        }
    }
    public List<User> getUserData() {
        return userData;
    }

}

