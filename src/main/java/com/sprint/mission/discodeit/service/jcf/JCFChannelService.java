package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final List<Channel> data = new ArrayList<>();

    @Override
    public void createChannel(String name, List<User> members) {
        Channel channel = new Channel(name, members);
        data.add(channel);
    }

    @Override
    public Optional<Channel> findChannel(UUID id) {
        Optional<Channel> channel = data.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst();
        return channel;
    }

    @Override
    public List<Channel> findAllChannels() {
        return data;
    }

    @Override
    public void updateChannelName(UUID id, String newName) {
        findChannel(id).ifPresentOrElse(c -> c.updateName(newName), () -> {
            throw new IllegalArgumentException("channel not found: " + id);
        });
    }

    @Override
    public void updateMember(UUID id, List<User> members) { //유저 검증도 필요할 듯
        findChannel(id).ifPresentOrElse(c -> c.updateMembers(members), () -> {
            throw new IllegalArgumentException("channel not found: " + id);
        });
    }

    @Override
    public void sendMessage(UUID id, Message message) {
        findChannel(id).ifPresentOrElse(c -> c.getMessages().add(message), () -> {
            throw new IllegalArgumentException("channel not found: " + id);
        });
    }

    @Override
    public void removeChannel(UUID id) {
        findChannel(id).ifPresent(data::remove);
    }
}
