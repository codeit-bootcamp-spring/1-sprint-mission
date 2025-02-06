package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<User, List<Channel>> channelData;

    public JCFChannelRepository(Map<User, List<Channel>> channelData) {
        this.channelData = channelData;
    }


    @Override
    public Channel save(Channel channel) {
        List<Channel> channels = new ArrayList<>();
        channels.add(channel);
        channelData.put(channel.getUser(), channels);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(channelData.values().stream()
                .flatMap(List::stream)
                .filter(channel -> channel.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found Channel ")));
    }

    @Override
    public List<Channel> findAll() {
        return channelData.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteByChannel(Channel channel) {
        List<Channel> channels = channelData.get(channel.getUser());
        if(channels != null) {
            channels.remove(channel);
            if(channels.isEmpty()) {
                channelData.remove(channel.getUser());
            }
        }
    }


    @Override
    public boolean existsByUser(User user) {
        return channelData.values().stream()
                .flatMap(List::stream)
                .anyMatch(channel -> channel.getUser().equals(user));
    }




}
