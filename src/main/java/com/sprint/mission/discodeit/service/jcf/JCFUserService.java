package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    private final Map<UUID,User> data;

    public JCFUserService() {
        data = new HashMap<>();
    }

    // Create User
    @Override
    public User createUser(String name, String email) {
        if (correctName(name) && correctEmail(email)) {
            User newUser = new User(name, email);
            data.put(newUser.getId(),newUser);
            System.out.println("환영합니다! " + newUser.getName() + "님 반갑습니다.");
            return newUser;
        }
        return null;
    }

    // 모든 유저 조회
    @Override
    public List<User> getAllUserList() {
        return data.values().stream().collect(Collectors.toList());
    }

    // ID으로 유저찾기
    @Override
    public User searchById(UUID userId) {
        User user = data.get(userId);
       if (user == null) {
           System.out.println("해당 사용자가 존재하지 않습니다.");
       }
        return user;
    }

    // 유저 삭제
    @Override
    public void deleteUser(User deleteUser) {
        data.remove(deleteUser);
        System.out.println("탈퇴되었습니다.");
    }

    //하나의 유저 정보 출력
    @Override
    public void printUserInfo(User user) {
        System.out.println(user);
    }

    //유저 정보 list로 출력
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

    //내부에서 사용하는 이름 -> 유효성 검사
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

    //유효성 검사 -> 이메일 형식
    private boolean correctEmail(String email) {
        String emailFormat = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailFormat)) {
            System.out.println("이메일 형식이 올바르지 않습니다.");
        }
        return email.matches(emailFormat);
    }
}