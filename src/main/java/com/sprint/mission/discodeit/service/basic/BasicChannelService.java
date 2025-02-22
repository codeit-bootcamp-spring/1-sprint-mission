package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequest;
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
        return ChannelResponse.fromEntity(channel);
    }

    @Override
    public List<ChannelResponse> getChannels() {
        return channelRepository.getAllChannels().stream()
                .map(ChannelResponse::fromEntity)
                .toList();
    }

    @Override
    public Optional<ChannelResponse> getChannel(UUID uuid) {
        return channelRepository.getChannelById(uuid)
                .map(ChannelResponse::fromEntity);
    }

    @Override
    public Optional<ChannelResponse> addMessageToChannel(UUID channelUUID, UUID messageUUID) {
        return channelRepository.getChannelById(channelUUID).map(channel -> {
            channel.addMessageToChannel(messageUUID);
            return channelRepository.save(channel);
        }).map(ChannelResponse::fromEntity);
    }

    @Override
    public List<UUID> getMessagesUUIDFromChannel(UUID uuid) {
        return channelRepository.getChannelById(uuid).orElseThrow().getMessageList();
    }


    @Override
    public Optional<ChannelResponse> updateChannel(UUID uuid, String channelName) {
        return channelRepository.getChannelById(uuid).map(channel -> {
            channel.updateChannelName(channelName);
            return channelRepository.save(channel);
        }).map(ChannelResponse::fromEntity);
    }

    @Override
    public void deleteChannel(UUID uuid) {
        channelRepository.getChannelById(uuid).map(channel -> {
            channelRepository.deleteChannel(uuid);
            channelRepository.save();
            return null;
        });
    }

    @Override
    public ChannelResponse createPrivateChannel(CreatePrivateChannelRequest request) {
        Channel channel = channelRepository.save(new Channel(true));
        return ChannelResponse.fromEntity(channel);
    }
}
