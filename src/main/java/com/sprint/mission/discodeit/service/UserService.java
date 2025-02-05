package com.sprint.mission.discodeit.service;
import com.sprint.mission.discodeit.entity.User;

import java.util.Collection;
import java.util.UUID;

public interface UserService {
    UUID createUser(String nickname);
    // Read : 전체 유저 조회, 특정 유저 조회
    Collection<User> showAllUsers(); //나중? 다음?에 Users 자체를 반환하도록 변경
    User getUserById(UUID id);

    // Update : 특정 유저 닉네임 변경
    void updateUserNickname(UUID id);

    // Delete : 전체 유저 삭제, 특정 유저 삭제
    void clearAllUsers();
    void removeUserById(UUID id);

}
