package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    Map<UUID, Channel> channels;
    public JCFChannelRepository(){
        this.channels = new HashMap<>();
    }
    @Override
    public Channel saveChannel(Channel channel){
        channels.put(channel.getId(), channel);
        return channel;
    }
    @Override
    public Optional<Channel> findChannelById(UUID id){
        // nullable 한 Optional 로 감싸서 null-safety를 보장
        return Optional.ofNullable(channels.get(id));
    }
    @Override
    public Collection<Channel> getAllChannels(){
        return channels.values();
    }

    @Override
    public void deleteChannelById(UUID id){
        // 추가적으로 구현할 수 있는 로직 : Channel이 message를 가지고 있다면,
        // Channel의 하위 메세지도 삭제하기

        // !! 고민 !!
        // (1) Channel 에 Message 객체들을 가리키는 ArrayList 를 만들어 삭제시키기
        // 단점이라고 생각되는 부분은, Channel에서 하는 일이 많아지지 않을까? 하는 것.
        // + 그렇다면, Message에서 Channel을 지정하는 부분과 역할(Message가 어떤 Channel에 소속되어 있는지) 자체가
        // 동일해지니 Message 도메인 측에 Channel을 지정하는 부분을 없애고 (1)을 진행해볼지에 대한 것
        
        // (2) Channel, Message 구조를 그대로 유지하고, 삭제할 Channel을 지정한 Message를 찾아 삭제시키기
        // 단점이라고 생각되는 부분은, 검색하고 찾는 것에서도 시간이 걸릴 거라는 것.
        channels.remove(id);
    }
}
