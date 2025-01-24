package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID,Channel> data;

    public JCFChannelRepository() {
        this.data = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public void createChannel(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> getChannelById(UUID id) {
        Channel channelNullable=this.data.get(id);
        return Optional.ofNullable(Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with " + id + " not found")));
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(UUID id, Channel updatedChannel) {
        Channel existingChannel = data.get(id);
        if (existingChannel != null) {
            existingChannel.update(updatedChannel.getChannel(),updatedChannel.getDescription());
        }
    }

    @Override
    public void deleteChannel(UUID id) {
        if(!this.data.containsKey(id)){
            throw new NoSuchElementException("Channel with id"+id+" not found");
        }
        data.remove(id);
    }
}
