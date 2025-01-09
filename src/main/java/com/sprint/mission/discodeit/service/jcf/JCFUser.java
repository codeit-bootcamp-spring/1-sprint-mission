package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class JCFUser implements UserService {
    private final List<User> data;

    public JCFUser() {
        data = new ArrayList<>();
    }

    // 유저 생성
    @Override
    public User createUser(String name, String email){
        User newUser = new User(name, email);
        data.add(newUser);
        return newUser;
    }

    // 모든 유저 조회
    @Override
    public void getAllUserList() {
        for (User user : data) {
            System.out.println(user.displayInfoUser());
        }
    }

    @Override
    public void getUserInfo(User user) {
        System.out.println(user.displayInfoUser());
    }

    // 이름으로 유저찾기
    @Override
    public User searchByUserName(String userName) {
        for (User user : data) {
            if (user.getName().equals(userName)) {
                return user;
            }
        }
        System.out.println("존재하지 않는 유저입니다.");
        return null;
    }

    // 유저 삭제
    @Override
    public void deleteUser(User deleteUser) {
        for (User user: data) {
            if (user == deleteUser) {
                data.remove(user);
            }
        }
    }

    // ? 유저 이름 업데이트
    @Override
    public void updateUserName(User user ,String newName) {
        user.updateName(newName);
    }

    // ? 유저 이메일 업데이트
    @Override
    public void updateUserEmail(User user, String newEmail) {
        user.updateEmail(newEmail);
    }
}
