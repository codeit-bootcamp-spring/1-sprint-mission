package com.sprint.mission.discodeit.service;


import java.util.ArrayList;
import java.util.UUID;
import com.sprint.mission.discodeit.entity.Channel;

public interface ChannelService {
    //생성
    Channel createChannel(String channelName, String description);

    // 수정
    Channel updateName(Channel channel, String name);
    Channel updateDescription(Channel channel, String description);

    // 조회
    Channel findChannel(UUID id);
    ArrayList<Channel> findAllChannels();

    // 프린트
    void printChannel(Channel channel);
    void printAllChannels(ArrayList<Channel> channels);

    // 삭제
    void deleteChannel(Channel channel);
}
