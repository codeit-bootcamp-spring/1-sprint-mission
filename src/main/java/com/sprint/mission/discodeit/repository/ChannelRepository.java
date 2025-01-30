package com.sprint.mission.discodeit.repository;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;

import java.util.List;

public interface ChannelRepository {

    Channel saveChannel(Channel channel); // 채널 저장

    void deleteChannel(Channel channel); // 채널 삭제

    List<Channel> printUser(User user); // 특정 유저의 채널 조회

    List<Channel> printAllChannel(); // 전체 채널 조회
}
