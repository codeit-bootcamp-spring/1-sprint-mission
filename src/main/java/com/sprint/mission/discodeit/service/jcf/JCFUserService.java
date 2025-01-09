package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.UserEntity;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<UserEntity> data = new ArrayList<>();

    @Override
    public void addUser(UserEntity user) {
        data.add(user);
    }

    @Override
    public UserEntity getUserById(UUID id) {
        return data.stream().filter(user -> user.getId().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public UserEntity getUserByIdWithFlg(UUID id) {
        return data.stream().filter(user -> user.getId().equals(id) && user.getUserDelFlg().equals("0"))
                .findFirst().orElse(null);
    }

    @Override
    public void updateUsername(UUID id, String username) {
        UserEntity user = getUserById(id);
        if(user != null){
            user.updateUsername(username);
            user.updateUpdatedAt(System.currentTimeMillis());
            System.out.println("username 이 갱신되었습니다.");
        } else {
            System.out.println("username 갱신 중에 오류가 발생하였습니다. 다시 시도해주세요.");
        }
    }

    @Override
    public void updateEmail(UUID id, String email) {
        UserEntity user = getUserById(id);
        if (user != null){
            user.updateUsername(email);
            user.updateUpdatedAt(System.currentTimeMillis());
            System.out.println("Email 이 갱신되었습니다.");
        } else {
            System.out.println("Email 갱신 중에 오류가 발생하였습니다. 다시 시도해주세요.");
        }
    }

    @Override
    public void updateUser(UUID id, String username, String email) {
        UserEntity user = getUserById(id);
        if (user != null){
            user.updateUsername(username);
            user.updateEmail(email);
            user.updateUpdatedAt(System.currentTimeMillis());
            System.out.println("username 과 Email 이 갱신되었습니다.");
        } else {
            System.out.println("username 과 email 갱신 중에 오류가 발생하였습니다. 다시 시도해주세요.");
        }

    }

    @Override
    public void deleteUserByUserId(UUID id) {
        if(getUserById(id) != null){
            data.removeIf(user -> user.getId().equals(id));
        }
    }

    @Override
    public void deleteUserByUserIdWithFlg(UUID id) {
        UserEntity user = getUserById(id);
        if(user != null){
            user.updateUserDelFlg("1");
            user.updateUpdatedAt(System.currentTimeMillis());
        }
    }
}
