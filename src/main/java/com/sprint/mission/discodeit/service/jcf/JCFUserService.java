package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final Map<UUID, User> userList;

    public JCFUserService() {
        this.userList = new HashMap<>();
    }

    @Override
    public User createUser(String userName) { // 유저 추가
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 null 또는 빈 문자열일 수 없습니다.");
        }
        User user = new User(userName);
        userList.put(user.getId(), user);
        System.out.println(DateUtils.transTime(user.getCreatedAt()) + " " + user.getUserName() + " 유저가 생성되었습니다.");
        return user;
    }

    @Override
    public User readUser(UUID id) { // 유저 읽기
        return Optional.ofNullable(userList.get(id))
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. ID: " + id));
    }

    @Override
    public List<User> readAllUser() { // 모든 유저 읽기
        return new ArrayList<>(userList.values());
    }

    @Override
    public User modifyUser(UUID userID, String newName) { // 유저 수정
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("유저 이름은 null 또는 빈 문자열일 수 없습니다.");
        }
        User user = readUser(userID);
        String oriName = user.getUserName();
        user.updateUsername(newName);
        user.updateUpdatedAt();
        System.out.println("유저 이름 변경: \"" + oriName + "\" -> \"" + newName + "\"");
        return user;
    }

    @Override
    public void deleteUser(UUID id) { // 유저 삭제
        User user = readUser(id);
        if (userList.remove(id) != null) {
            System.out.println(user.getUserName() + " 유저가 삭제되었습니다.");
        } else {
            System.out.println(user.getUserName() + " 유저 삭제 실패하였습니다.");
        }
    }
}
