package com.sprint.misson.discordeit.service.jcf;

import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.ChannelType;
import com.sprint.misson.discordeit.service.ChannelService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {

    final HashMap<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    //생성
    @Override
    public Channel CreateChannel(String name, ChannelType type) {
        Channel channel = new Channel( name, type );
        data.put( channel.getId(), channel );
        return channel;
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

    //다건 조회 - 채널 타입
    @Override
    public List<Channel> getChannelByType(ChannelType channelType) {
        return data.values().stream()
                .filter( c -> c.getChannelType().equals( channelType ))
                .collect( Collectors.toList());
    }

    //다건 조회 - 채널명
    @Override
    public List<Channel> getChannelsByName(String channelName) {
        return data.values().stream().filter(
                c->c.getChannelName().contains( channelName )
        ).collect( Collectors.toList());
    }
    //수정
    @Override
    public boolean updateChannel(Channel channel) {
        if ( channel.getId() == null ) {
            return false;
        }
        channel.updateUpdatedAt();
        data.put( channel.getId(), channel );
        return true;
    }

    //삭제
    @Override
    public boolean deleteChannel(Channel channel) {
        return data.remove(channel.getId()) != null ;
    }
}
