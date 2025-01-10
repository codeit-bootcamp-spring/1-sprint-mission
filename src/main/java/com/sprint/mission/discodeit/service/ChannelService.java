package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    // 새로운 채널 생성
    Channel createChannel(String title, User owner);

    // 모든 채널 조회
    List<Channel> getAllChannelList();

    // 채널ID로 채널 하나만 검색
    Channel searchById(UUID channelId);

    // 채널의 전체 정보 출력
    void printChannelInfo(Channel channel);
    void printChannelListInfo(List<Channel> channelList);

    // 채널 명 수정
    void updateTitle(Channel channel,String channelName);

    // 채널 삭제
    void deleteChannel(Channel channel);

    // 채널의 모든 멤버 조회
    List<User> getAllMemberList(Channel channel);

    // 채널 멤버 추가 또는 삭제 _> 아직 구현 X
    void addMember(Channel channel, User user);
    void deleteMember(Channel channel, User user);

}