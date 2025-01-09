package com.sprint.misson.discordeit.service;

import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.Message;

import java.util.List;

public interface ChannelService {

    //생성
    public boolean CreateChannel(Channel channel);

    //모두 읽기
    public List<Channel> getChannels();

    //읽기
    //단건 조회 - UUID
    public Channel getChannelByUUID(String channelId);

    //다건 조회 - 특정 채널의 메세지 목록 조회
    public List<Message> getChannelMessages(Channel channel);

    //수정
    public boolean updateChannel(Channel channel);

    //삭제
    public boolean deleteChannel(Channel channel);
}
