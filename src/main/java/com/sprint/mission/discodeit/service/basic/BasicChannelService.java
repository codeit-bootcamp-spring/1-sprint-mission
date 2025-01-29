package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BaseRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository repository;

    public BasicChannelService(ChannelRepository repository){
        this.repository = repository;
    }


    @Override
    public Channel create(Channel channel) {
        repository.save(channel);
        return channel;
    }

    @Override
    public Channel readOne(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<Channel> readAll() {
        return repository.readAll();
    }

    @Override
    public Channel update(UUID id, Channel updateChannel) {
        return repository.modify(id, updateChannel);
    }

    @Override
    public boolean delete(UUID id) {
        return repository.deleteById(id);
    }

    @Override
    public Channel channelOwnerChange(UUID id, User owner) {
        return repository.ownerChange(id, owner);
    }

    @Override
    public boolean channelMemberJoin(UUID id, User user) {
        return repository.memberJoin(id, user);
    }

    @Override
    public boolean channelMemberWithdrawal(UUID id, User user) {
        return repository.memberWithdrawal(id, user);
    }
}
