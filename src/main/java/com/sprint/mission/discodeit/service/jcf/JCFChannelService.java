package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.impl.InMemoryChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public JCFChannelService() {
        this.channelRepository = new InMemoryChannelRepository();
    }

    @Override
    public void createChannel(String name, List<User> members) {
        Channel channel = new Channel(name, members);
        channelRepository.save(channel);
    }

    @Override
    public Channel findChannelById(UUID id) {
        return channelRepository.findChannelById(id)
            .orElseThrow(() -> new IllegalArgumentException("channel not found: " + id));
    }

    @Override
    public List<Channel> findAllChannels() {
        return channelRepository.findAllChannels();
    }

    @Override
    public void updateChannelName(UUID id, String newName) {
        channelRepository.findChannelById(id)
            .ifPresentOrElse(c -> c.updateName(newName), () -> {
                throw new IllegalArgumentException("channel not found: " + id);
            });
    }

    @Override
    public void updateMember(UUID id, List<User> members) { //유저 검증도 필요할 듯
        channelRepository.findChannelById(id)
            .ifPresentOrElse(c -> c.updateMembers(members), () -> {
                throw new IllegalArgumentException("channel not found: " + id);
            });
    }

    @Override
    public void sendMessage(UUID id, Message message) {
        channelRepository.findChannelById(id)
            .ifPresentOrElse(c -> c.getMessages().add(message), () -> {
                throw new IllegalArgumentException("channel not found: " + id);
            });
    }

    @Override
    public void removeChannel(UUID id) {
        channelRepository.remove(id);
    }
}
