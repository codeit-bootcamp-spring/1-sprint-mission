package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelService {
    //채널 생성
    void createChannel(Channel channel);
    //채널 삭제
    void deleteChannel(Channel channel);
    //채널 수정
    void updateChannel(Channel channel);
    //채널 목록 확인
    List<Channel> getAllChannel();

}
