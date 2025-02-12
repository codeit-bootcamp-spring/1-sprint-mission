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

    // 채널 명 수정
    void updateChannelName(UUID channelId, String channelName);

    // 채널 삭제
    void deleteChannel(UUID channelId);


    void createChannel(Channel channel);
}