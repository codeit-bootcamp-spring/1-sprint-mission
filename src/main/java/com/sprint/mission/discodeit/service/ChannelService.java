package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    // 채널 생성
    Channel createChannel(String title, User owner);

    // 모든 채널 조회
    List<Channel> getAllChannelList();

    // 채널ID로 조회
    Channel searchById(UUID channelId);

    // 채널 정보 출력
    void printChannelInfo(Channel channel);
    void printChannelListInfo(List<Channel> channelList);

    // 채널명 업데이트
    void updateTitle(Channel channel,String channelName);

    // 채널 삭제
    void deleteChannel(Channel channel);

    // 채널의 모든 멤버 조회
    List<User> getAllMemberList(Channel channel);

    // 채널 멤버
    void addMember(Channel channel, User user);
    void deleteMember(Channel channel, User user);

}