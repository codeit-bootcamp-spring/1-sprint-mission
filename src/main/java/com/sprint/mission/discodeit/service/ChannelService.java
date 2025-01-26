package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    // 채널 생성 (이름으로 생성)
    Channel createChannel(String name);

    // 채널 단건 조회 (ID로 조회)
    Channel getChannelById(UUID channelId);

    // 채널 다건 조회 (모든 채널)
    List<Channel> getAllChannels();

    // 채널 이름 수정
    Channel updateChannelName(UUID channelId, String newName);

    // 채널 삭제
    boolean deleteChannel(UUID channelId);

    void addUserToChannel(UUID channelId, UUID userId);

}


