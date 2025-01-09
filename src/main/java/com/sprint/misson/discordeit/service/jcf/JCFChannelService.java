package com.sprint.misson.discordeit.service.jcf;

import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.Message;
import com.sprint.misson.discordeit.service.ChannelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    final HashMap<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    //생성
    @Override
    public boolean CreateChannel(Channel channel) {

        data.put( channel.getId(), channel );
        return true;
    }

    //모두 조회
    @Override
    public List<Channel> getChannels() {
        return new ArrayList<>( data.values() );
    }

    //단일 조회 - UUID
    @Override
    public Channel getChannelByUUID(String channelId) {
        return data.get( UUID.fromString( channelId ) );
    }

    //다건 조회 - 특정 채널의 메세지 목록 조회
    @Override
    public List<Message> getChannelMessages(Channel channel){
        return data.get( channel.getId() ).getMessages();
    }

    //수정
    @Override
    public boolean updateChannel(Channel channel) {
        if ( channel.getId() == null ) {
            return false;
        }
        data.put( channel.getId(), channel );
        return true;
    }

    //삭제
    @Override
    public boolean deleteChannel(Channel channel) {
        return data.remove(channel.getId()) != null ;
    }
}
