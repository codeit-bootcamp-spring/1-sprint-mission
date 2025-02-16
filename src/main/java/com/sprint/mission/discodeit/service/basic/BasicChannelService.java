package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public ChannelResponse createChannel(CreateChannelRequest request) {
        Channel channel = channelRepository.save(new Channel(request.channelName()));
        return new ChannelResponse(channel.getChannelName());
    }

    @Override
    public List<ChannelResponse> getChannels() {
        return channelRepository.getAllChannels().stream().map(channel -> new ChannelResponse(channel.getChannelName())).collect(Collectors.toList());
    }

    @Override
    public Optional<ChannelResponse> getChannel(UUID uuid) {
        return Optional.ofNullable(channelRepository.getChannelById(uuid)).map(channel -> new ChannelResponse(channel.getChannelName()));
    }

    @Override
    public Optional<ChannelResponse> addMessageToChannel(UUID channelUUID, UUID messageUUID) {
        return Optional.ofNullable(channelRepository.getChannelById(channelUUID)).map(channel -> {
            channel.addMessageToChannel(messageUUID);
            return channelRepository.save(channel);
        }).map(channel -> new ChannelResponse(channel.getChannelName()));
    }

    @Override
    public List<UUID> getMessagesUUIDFromChannel(UUID uuid) {
        return Optional.ofNullable(channelRepository.getChannelById(uuid)).orElseThrow().getMessageList();
    }


    @Override
    public Optional<ChannelResponse> updateChannel(UUID uuid, String channelName) {
        return Optional.ofNullable(channelRepository.getChannelById(uuid)).map(channel -> {
            channel.updateChannelName(channelName);
            return channelRepository.save(channel);
        }).map(channel -> new ChannelResponse(channel.getChannelName()));
    }

    @Override
    public void deleteChannel(UUID uuid) {
        Optional.ofNullable(channelRepository.getChannelById(uuid)).map(channel -> {
            channelRepository.deleteChannelById(uuid);
            channelRepository.save();
            return null;
        });
    }
}
