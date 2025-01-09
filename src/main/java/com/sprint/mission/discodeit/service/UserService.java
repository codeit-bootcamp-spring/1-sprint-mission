package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    // 유저 생성
    User createUser(String name, String email);

    // 유저 정보 수정: 닉네임, 이메일
    void updateUserName(String name);
    void updateUserEmail(String email);

    // 유저 정보 출력
    void displayInfoUser();

    // 유저 채널 조회
    List<Channel> getAllChannelList();
}
