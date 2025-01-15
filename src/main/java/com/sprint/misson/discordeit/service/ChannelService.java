package com.sprint.misson.discordeit.service;

import com.sprint.misson.discordeit.dto.ChannelDTO;
import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.ChannelType;
import com.sprint.misson.discordeit.entity.User;

import java.util.List;

public interface ChannelService {

    //생성
    Channel CreateChannel(String name, ChannelType type);

    //모두 읽기
    List<Channel> getChannels();

    //읽기
    //단건 조회 - UUID
    Channel getChannelByUUID(String channelId);

    //다건 조회 - name
    List<Channel> getChannelsByName(String channelName);

    //다건 조회 - 채널 타입
    List<Channel> getChannelByType(ChannelType channelType);

    //수정
    Channel updateChannel(String channelId, ChannelDTO channelDTO);

    //삭제
    boolean deleteChannel(Channel channel);

    List<User> getUsersInChannel(Channel channel);

    boolean addUserToChannel(Channel channel, User user);

    boolean deleteUserFromChannel(Channel channel, User user);

    boolean isUserInChannel(Channel channel, User user);

}
