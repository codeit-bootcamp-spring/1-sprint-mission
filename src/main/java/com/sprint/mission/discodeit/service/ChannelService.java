package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChannelService {// 생성
    void craete(UUID ownerId, String category, String name, String explanation);

    // 읽기
    Channel read(UUID id);

    // 모두 읽기
    List<Channel> allRead();

    // 수정
    void updateCategory(UUID id, String category);
    void updateName(UUID id, String name);
    void updateExplanation(UUID id, String explanation);

    // 멤버 수정
    // 멤버가 List에 있을 경우 멤버 삭제, 없을 경우 추가
    void updateMembers(UUID id, UUID memberId);

    // 삭제
    void delete(UUID id);
}
