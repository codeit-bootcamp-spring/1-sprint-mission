package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JCF_channel implements ChannelService {
    private final List<Channel> channelSet;

    public JCF_channel() {
        this.channelSet = new ArrayList<>();
    }

    public boolean isDuplication(List<Channel> channelSet, String title) {
        return channelSet.stream().anyMatch(channel1 -> channel1.getTitle().equals(title));
    }

    @Override
    public void creat(Channel channel) {
        if (isDuplication(channelSet, channel.getTitle())) {
            System.out.println("Title duplication!");
        }
        else {
            channelSet.add(channel);
        }
    }

    @Override
    public void delete(Channel channel) {
        channelSet.remove(channel);

    }

    @Override
    public void update(Channel channel, String title) {
        channelSet.forEach(channel1 -> {
            if (channel1.getId().equals(channel.getId()) && !(isDuplication(channelSet, title))) {
                channel1.updateTitle(title);
            }});

    }


    @Override
    public Channel write(String title) {
        Optional<Channel> channel = channelSet.stream().filter(channel_id -> channel_id.getTitle().equals(title)).findFirst();

        if(channel.isPresent()){
            return channel.get();
        }
        else{
            return null;
        }
    }

    @Override
    public List<Channel> allWrite() {
        return channelSet;

    }

}
