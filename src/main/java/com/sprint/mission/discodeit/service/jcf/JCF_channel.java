package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCF_channel implements ChannelService {
    private final List<Channel> channelList;

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
    public void delete(UUID channelId) {
        Optional<Channel> getChannel = channelSet.stream().filter(channel1 -> channel1.getId().equals(channelId)).findFirst();
        Channel channel = getChannel.get();
        channelSet.remove(channel);

    }

    @Override
    public void update(UUID channelId, String title) {
        channelSet.stream()
                .filter(channel -> channel.getId().equals(channelId))
                .forEach(channel -> channel.updateTitle(title));

    }


    @Override
    public UUID get(String title) {
        Optional<Channel> channel = channelSet.stream().filter(channel_id -> channel_id.getTitle().equals(title)).findFirst();

        if(channel.isPresent()){
            return channel.get().getId();
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
