package com.sprint.misson.discordeit.service;

import com.sprint.misson.discordeit.dto.ChannelDTO;
import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.ChannelType;
import com.sprint.misson.discordeit.entity.User;

import java.util.List;

public interface ChannelService {

    //생성
    Channel create(ChannelType channelType, String name, String description);

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
    Channel updateChannel(String channelId, ChannelDTO channelDTO, long updatedAt);

    //삭제
    boolean deleteChannel(Channel channel);

    List<User> getUsersInChannel(Channel channel);

    //고민
    //근데 String으로 id만 받는게 맞는것 같다.
    //중간에 객체 자체를 수정해버리는 문제가 생길수도 있을 것 같음?
    boolean addUserToChannel(String channelId, String userId);

    boolean deleteUserFromChannel(Channel channel, User user);

    boolean isUserInChannel(Channel channel, User user);

}
