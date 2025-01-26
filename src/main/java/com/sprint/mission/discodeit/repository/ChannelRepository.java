package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    //Channel 생성
    Channel createChannel (String title, User owner);
    //모든 채널 조회
    List<Channel> getAllChannelList();

    //ChannelId로 하나만 조회
    Channel searchById(UUID channelId);

    //ChannelName 수정
    void updateChannelName(UUID channelId, String channelName);

    //Channel 삭제
    void deleteChannel(UUID channelId);

    /*//Channel 에 있는 모든 멤버 조회
    List<User> getAllMemberList(Channel channel);

    //Channel에 멤버 추가 또는 삭제
    void addMember(Channel channel, User user);
    void deleteMember(Channel channel, User user);*/
}
