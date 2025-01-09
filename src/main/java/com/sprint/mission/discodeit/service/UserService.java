package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // 유저 생성
    User createUser(String name, String email);

    // 유저 정보 수정: 닉네임, 이메일
    void updateUserName(User user, String name);
    void updateUserEmail(User user, String email);

    // 모든 유저 조회
    List<User> getAllUserList();
    
    // 이름으로 유저 조회
    User searchByUserName(String userName);
    
    // 유저 삭제
    void deleteUser(User user);

}
