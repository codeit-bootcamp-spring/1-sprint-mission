package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCFUserService implements UserService {
    private final Map<String, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public void createUser(User user) {
        if(data.containsKey(user.getId().toString())) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다 ." + user.getId());
        }
        data.put(user.getId().toString(), user);
        System.out.println("사용자가 생성되었습니다. ID: " + user.getId() + ", Created At: " + user.getCreatedAt());
    }
    @Override
    public User readUser(String id) {
        User user = data.get(id);
        if(user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다. : " + id);
        }
        return user;
    }

    @Override
    public List<User> readAllUsers() {
        return new ArrayList<>(data.values()); // Map의 모든 값(User 객체)을 List로 반환
    }
    @Override
    public void updateUser(User user) {
        if(!data.containsKey(user.getId().toString())) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다. : " + user.getId());
        }
        data.put(user.getId().toString(), user);
        System.out.print("사용자 정보가 업데이트되었습니다. : " + user.getId() + ", Updated At: " + user.getUpdatedAt());
    }

    public void deleteUser(String id) {
        if(!data.containsKey(id)) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다. : " + id );
        }
        data.remove(id);
        System.out.println("사용자가 삭제되었습니다. :" + id);
    }


}
