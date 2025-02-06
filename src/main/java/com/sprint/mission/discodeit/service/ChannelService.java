package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface ChannelService {

    //생성
    Channel create(CreateChannelDto createChannelDto);

    //모두 읽기
    List<Channel> findAll();

    //읽기
    //단건 조회 - UUID
    Channel findById(String channelId);

    //다건 조회 - name
    List<Channel> findAllByChannelName(String channelName);

    //다건 조회 - 채널 타입
    List<Channel> findByChannelType(ChannelType channelType);

    //수정
    Channel updateChannel(String channelId, UpdateChannelDto updateChannelDto);

    //삭제
    boolean deleteChannel(Channel channel);

    List<User> findAllUserInChannel(Channel channel);

    //고민
    //근데 String으로 id만 받는게 맞는것 같다.
    //중간에 객체 자체를 수정해버리는 문제가 생길수도 있을 것 같음?
    boolean addUserToChannel(String channelId, String userId);

    boolean deleteUserFromChannel(Channel channel, User user);

    boolean isUserInChannel(Channel channel, User user);

}
