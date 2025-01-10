package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final List<Channel> channels = new ArrayList<>();

    @Override
    public void createChannel(String name, List<User> members) {
        Channel channel = new Channel(name, members);
        channels.add(channel);
    }

    @Override
    public Channel findChannel(UUID id) {
        Channel channel = channels.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElse(null);
        return channel;
    }

    @Override
    public List<Channel> findAllChannels() {
        return channels;
    }

    @Override
    public void updateChannelName(UUID id, String newName) {
        channels.stream().filter(c -> c.getId().equals(id))
            .findFirst()
            .ifPresentOrElse(channel -> channel.updateName(newName), () -> System.out.println("channel not found"));
    }

    @Override
    public void updateMember(UUID id, List<User> members) {
        channels.stream().filter(c -> c.getId().equals(id))
            .findFirst()
            .ifPresentOrElse(channel -> channel.updateMembers(members), () -> System.out.println("channel not found"));
    }

    @Override
    public void sendMessage(UUID id, Message message) {
        channels.stream().filter(c -> c.getId().equals(id))
            .findFirst()
            .ifPresentOrElse(channel -> channel.addMessage(message), () -> System.out.println("channel not found"));
    }

    @Override
    public void removeChannel(UUID id) {
        channels.stream().filter(c -> c.getId().equals(id))
            .findFirst()
            .ifPresentOrElse(channels::remove, () -> System.out.println("channel not found"));
    }
}
