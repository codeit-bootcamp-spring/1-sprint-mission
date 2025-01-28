package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;
    private final UserValidator userValidator = new UserValidator();

    public JCFUserService() {
        data = new HashMap<>();
    }

    // 유저 생성
    @Override
    public User createUser(String name, String email, String password) {
        if (userValidator.isValidName(name) && userValidator.isValidEmail(email)) {
            User newUser = new User(name, email, password);
            data.put(newUser.getId(), newUser);
            System.out.println("user create: " + newUser.getId());
            return newUser;
        }
        return null;
    }

    // 모든 유저 조회
    @Override
    public List<User> getAllUserList() {
        return data.values().stream().toList();
    }

    // ID으로 유저찾기
    @Override
    public User searchById(UUID userId) {
        if (data.containsKey(userId)) { // 데이터 조회를 repository랑 분리하면 괜찮을 것 같음...?
            return data.get(userId);
        } else {
            System.out.println("user dose not exist");
            return null;
        }
    }

    // 유저 삭제
    @Override
    public void deleteUser(UUID userId) {
        if (data.containsKey(userId)) {
            data.remove(userId);
            System.out.println("success delete");
        }
    }

    // 유저 이름 업데이트
    @Override
    public void updateUserName(UUID userId, String newName) {
        if (data.containsKey(userId)) {
            if (userValidator.isValidName(newName)) {
                searchById(userId).updateName(newName);
                System.out.println("success update");
            }
        }
    }

    // 유저 이메일 업데이트
    @Override
    public void updateUserEmail(UUID userId, String newEmail) {
        if (data.containsKey(userId)) {
            if (userValidator.isValidEmail(newEmail)) {
                searchById(userId).updateEmail(newEmail);
                System.out.println("success update");
            }
        }
    }
}
