package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {// 생성
    void craete(Channel channel);

    // 읽기
    Channel read(UUID id);

    // 모두 읽기
    List<Channel> readAll();

    // 수정
    void updateCategory(UUID id, String updateCategory);
    void updateName(UUID id, String updateName);
    void updateExplanation(UUID id, String updateExplanation);

    // 멤버 수정
    // 멤버가 List에 있을 경우 멤버 삭제, 없을 경우 추가
    void addMember(UUID id, UUID memberId);
    void deleteMember(UUID id, UUID memberId);

    // 삭제
    void delete(UUID id);
    
    // 채널 존재 여부 확인
    void channelIsExist(UUID id);
}
