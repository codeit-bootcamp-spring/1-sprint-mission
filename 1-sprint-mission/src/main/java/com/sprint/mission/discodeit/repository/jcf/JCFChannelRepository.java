package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelRepository implements ChannelRepository {

    private final List<Channel>channelList = new ArrayList<>();


    @Override
    public void save(Channel channel) {
        if(channel == null || channel.getUserId() == null){
            throw new IllegalArgumentException(" Channel cannot be null. ");
        }
        channelList.add(channel);
        System.out.println("<<< Channel saved successfully >>>");
    }

    @Override
    public Channel findByUuid(String channelUuid) {
        if(channelUuid == null || channelUuid.isEmpty()){
            throw new IllegalArgumentException(" Channel cannot be null or empty. ");
        }
        return channelList.stream()
                .filter(channel -> channel.getChannelUuid().equals(channelUuid))
                .findFirst()
                .orElseThrow(() ->new IllegalArgumentException("Channel with UUID " +channelUuid + " not found."));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channelList);
    }

    @Override
    public void delete(String channelUuid) {
        boolean removed = channelList.removeIf(channel ->
                channel.getChannelUuid().equals(channelUuid));

        if (removed) {
            System.out.println("Channel with UUID " + channelUuid + " was deleted.");
        } else {
            System.out.println("Channel with UUID " + channelUuid + " not found.");
        }
    }
}
