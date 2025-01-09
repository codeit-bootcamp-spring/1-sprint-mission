package com.sprint.mission.discodeit.service.jcf;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final List<Channel> data;

    public JCFChannelService() {
        this.data = new ArrayList<>();
    }

    @Override
    public void createChannel(Channel channel) {
        data.add(channel);
    }

    @Override
    public Optional<Channel> getChannelById(UUID id) {
        return data.stream().filter(channel -> channel.getId().equals(id)).findFirst();
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data);
    }

    @Override
    public void updateChannel(UUID id, Channel updatedChannel) {
        for (int i=0; i<data.size(); i++) {
            if (data.get(i).getId().equals(id)) {
                // 기존 객체를 수정
                Channel existingChannel = data.get(i);
                existingChannel.update(updatedChannel.getChannel(), updatedChannel.getDescription());
                break;
            }
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        data.removeIf(channel -> channel.getId().equals(id));
    }
}
