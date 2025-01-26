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
            System.out.println("환영합니다! " + newUser.getUserName() + "님 반갑습니다.");
            return newUser;
        }
        return null;
    }

    @Override
    public void updateUserName(UUID userId, String newName) {
        User user = data.get(userId);
        if(user == null) {
            System.out.println("해당 사용자가 존재하지 않습니다.");
            return;
        }
        if(correctName(newName)) {
            user.setUserName(newName);
            System.out.println("사용자의 이름이 성공적으로 수정되었습니다 : " + newName);
        }

    }

    @Override
    public void updateUserEmail(UUID userId, String newEmail) {
        User user = data.get(userId);
        if(user == null) {
            System.out.println("해당 사용자가 존재하지 않습니다.");
            return;
        }
        if(correctEmail(newEmail)) {
            user.setUserEmail(newEmail);
            System.out.println("사용자의 이메일이 성공적으로 변경되었습니다." + newEmail);
        }
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

    @Override
    public void deleteUser(UUID userId) {
        User user = data.get(userId);
        if(user == null) {
            System.out.println("해당 사용자가 존재하지 않습니다.");
        } else  {
            data.remove(userId);
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