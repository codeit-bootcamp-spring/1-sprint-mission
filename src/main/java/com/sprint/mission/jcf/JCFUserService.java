package com.sprint.mission.jcf;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private static JCFUserService instance;
    private final List<User> userData = new ArrayList<>();

    // 생성자
    private JCFUserService() {}

    // 싱글톤 패턴
    public static JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }
        return instance;
    }


    // 계정 생성, 이메일 형식이 아닐 경우 예외 처리
    @Override
    public User createUser(String email, String name) {
        if (email.contains("@") && email.contains(".") && !userData.contains(email)) {
            User newUser = new User(UUID.randomUUID(), email, name);
            userData.add(newUser);
            System.out.println("\n***계정 생성 완료***");
            return newUser;
        } else {
            throw new IllegalArgumentException("**올바른 이메일 형식인지 확인해 주세요.**");
        }
    }


    // 이메일 변경, 이메일 형식이 아닐 경우 예외 처리
    @Override
    public void updateMail(User user, String mail) {
        if (mail.contains("@") && mail.contains(".") &&! user.getEmail().equals(mail)){
            user.setEmail(mail);
            System.out.println("\n***이메일 변경 완료***");
            System.out.println(user);
        } else {
            System.out.println("**올바른 이메일 형식인지 확인해 주세요.**");
        }
    }

    // 모든 유저 조회
    @Override
    public List<User> getSearchAllUser() {
        System.out.println("\n***사용자 정보 전부 조회***");
        for (User user : userData) {
            System.out.println(user);
        }
        return userData;
    }

    // 특정 유저 조회
    @Override
    public void searchUser(User user) {
        if(userData.contains(user)){
            System.out.println("\n***[사용자 정보]***");
            System.out.println(user);
        } else {
            System.out.println("**유저를 찾을 수 없습니다.**\n");
        }
    }


    // 계정 삭제, 유저를 찾을 수 없을 경우 예외 처리
    @Override
    public void deleteUser(User user) {
        if (userData.contains(user)) {
            System.out.println("\n***계정 삭제 완료***");
            userData.remove(user);
            System.out.println(user);
        } else {
            System.out.println("**유저를 찾을 수 없습니다.**\n");
        }
    }
    public List<User> getUserData() {
        return userData;
    }

}

