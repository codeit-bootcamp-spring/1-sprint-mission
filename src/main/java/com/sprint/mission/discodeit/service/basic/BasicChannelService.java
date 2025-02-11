package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BaseRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public ChannelResponse create(ChannelRequest request) {
        Channel channel = new Channel(
                request.name(),
                request.description(),
                request.member().stream()
                        .map(user -> new ChannelRequest(user.username(), user.)),
                request.owner());
        channelRepository.save(request);
        return ChannelResponse.fromEntity(request);
    }

    @Override
    public ChannelResponse readOne(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<ChannelResponse> readAll() {
        return channelRepository.readAll();
    }

    @Override
    public ChannelResponse update(UUID id, ChannelRequest updateChannel) {
        return channelRepository.modify(id, updateChannel);
    }

    @Override
    public boolean delete(UUID id) {
        return channelRepository.deleteById(id);
    }

    @Override
    public Channel channelOwnerChange(UUID id, User owner) {
        return channelRepository.ownerChange(id, owner);
    }

    @Override
    public boolean channelMemberJoin(UUID id, User user) {
        return channelRepository.memberJoin(id, user);
    }

    @Override
    public boolean channelMemberWithdrawal(UUID id, User user) {
        return channelRepository.memberWithdrawal(id, user);
    }
}
