package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
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
    List<ChannelResponseDto> findAllByUserId(String userId);

    //읽기
    //단건 조회 - UUID
    ChannelResponseDto findById(String channelId);

    //다건 조회 - name
    List<Channel> findAllByChannelName(String channelName);

    //다건 조회 - 채널 타입
    List<Channel> findByChannelType(ChannelType channelType);

    //수정
    Channel updateChannel(String channelId, UpdateChannelDto updateChannelDto);

    //삭제
    boolean delete(String channelId);

    List<User> findAllUserInChannel(Channel channel);

    boolean addUserToChannel(String channelId, String userId);

    boolean deleteUserFromChannel(String channelId, String userId);

    boolean isUserInChannel(String channelId, String userId);

}
