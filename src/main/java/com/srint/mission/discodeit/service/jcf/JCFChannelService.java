package com.srint.mission.discodeit.service.jcf;


import com.srint.mission.discodeit.entity.Channel;
import com.srint.mission.discodeit.entity.ChannelType;
import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.entity.User;
import com.srint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        data = new HashMap<>();
    }

    //db 로직
    public UUID save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel.getId();
    }

    public Channel findOne(UUID id) {
        return data.get(id);
    }

    public List<Channel> findAll() {
/*        if(data.isEmpty()){
            return Collections.emptyList(); // 빈 리스트 반환
        }*/
        return new ArrayList<>(data.values());
    }

    public UUID update(Channel channel){
        data.put(channel.getId(), channel);
        return channel.getId();
    }

    public UUID delete(UUID id) {
        data.remove(id);
        return id;
    }


    //서비스 로직
    @Override
    public UUID create(String name, String description, ChannelType type) {
        if (!Channel.validation(name, description)) {
            throw new IllegalArgumentException("잘못된 형식입니다.");
        }
        Channel channel = new Channel(name, description, type);
        return save(channel);
    }

    @Override
    public Channel read(UUID id) {
        Channel findChannel = findOne(id);
        return Optional.ofNullable(findChannel)
                .orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다."));
    }

    @Override
    public List<Channel> readAll() {
        return findAll();
    }

    @Override
    public Channel updateName(UUID id, String channelName) {
        Channel findChannel = findOne(id);
        findChannel.setName(channelName);
        update(findChannel);
        return findChannel;
    }

    @Override
    public Channel updateDescription(UUID id, String description){
        Channel findChannel = findOne(id);
        findChannel.setDescription(description);
        update(findChannel);
        return findChannel;
    }

    @Override
    public Channel updateType(UUID id, ChannelType type){
        Channel findChannel = findOne(id);
        findChannel.setType(type);
        update(findChannel);
        return findChannel;
    }

    @Override
    public UUID deleteChannel(UUID id) {
        Channel findChannel = findOne(id);
        return delete(findChannel.getId());
    }
}
