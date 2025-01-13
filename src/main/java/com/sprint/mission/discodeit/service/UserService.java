package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // 생성
    void craete(String email, String pw, String name, String nickname, String phoneNumber);

    // 읽기
    User read(UUID id);

    // 모두 읽기
    List<User> allRead();

    // 수정
    void updateEmail(UUID id, String updateEmail);
    void updatePassword(UUID id, String updatePassword);
    void updateName(UUID id, String updateName);
    void updateNickname(UUID id, String updateNickname);
    void updatePhoneNumber(UUID id, String updatePhoneNumber);

    // 삭제
    void delete(UUID id);
}
