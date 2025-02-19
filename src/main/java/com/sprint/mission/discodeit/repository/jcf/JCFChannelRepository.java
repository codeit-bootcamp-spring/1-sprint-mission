package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;


public class JCFChannelRepository implements ChannelRepository {
    private static JCFChannelRepository instance;
    private final Map<UUID, Channel> channels;

    private JCFChannelRepository(){
        this.channels = new HashMap<>();
    }

    public static JCFChannelRepository getInstance() {
        if (instance == null) {
            instance = new JCFChannelRepository();
        }
        return instance;
    }

    @Override
    public Channel save(Channel channel){
        this.channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(this.channels.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return this.channels.values().stream()
                .toList();
    }

    @Override
    public boolean existsId(UUID channelId) {
        return this.channels.containsKey(channelId);
    }

    @Override
    public void delete(UUID channelId){
        this.channels.remove(channelId);
    }

}