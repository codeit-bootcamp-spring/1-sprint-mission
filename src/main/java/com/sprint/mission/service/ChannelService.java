package com.sprint.mission.service;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;

import java.util.List;

public interface ChannelService {

    // 채널 생성
    Channel createChannel(User user, String channel);

    // 채널명 변경
    void updateChannel(User user, String channel, String afterChannelName);

    // 채널 조회
    List<Channel> getAllChannelList();

    // 채널 정보 출력
    void channelInfo(User user);

    //삭제
    void deleteChannel(String channelName);
}
