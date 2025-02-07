package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.ChannelRepository;

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
        channelData.put(channel.getOwner(), channels);
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
        List<Channel> channels = channelData.get(channel.getOwner());
        if(channels != null) {
            channels.remove(channel);
            if(channels.isEmpty()) {
                channelData.remove(channel.getOwner());
            }
        }
    }


    @Override
    public boolean existsByUser(User user) {
        return channelData.values().stream()
                .flatMap(List::stream)
                .anyMatch(channel -> channel.getOwner().equals(user));
    }


    @Override
    public List<Channel> findAllByType(ChannelType type) {
        return channelData.values().stream()
                .flatMap(List::stream)
                .filter(channel -> channel.getType() ==type)
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> findAllByOwnerAndType(User owner, ChannelType type) {
        return channelData.getOrDefault(owner, new ArrayList<>())
                .stream()
                .filter(channel -> channel.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return channelData.values().stream()
                .flatMap(List::stream)
                .filter(channel -> channel.getOwner().getId().equals(userId)|
                        (channel.getType() == ChannelType.PRIVATE))
                .collect(Collectors.toList());
    }
}
