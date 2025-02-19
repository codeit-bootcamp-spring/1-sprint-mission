package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.collection.Channels;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JCFChannelService implements ChannelService {
    private final Channels channels = new Channels();

    @Override
    public ChannelResponse createChannel(CreateChannelRequest request) {
        Channel newChannel = new Channel(request.channelName());
        channels.add(newChannel);
        return new ChannelResponse(newChannel.getChannelName());
    }

    @Override
    public Optional<ChannelResponse> getChannel(UUID uuid) {
        return channels.get(uuid)
                .map(channel -> new ChannelResponse(channel.getChannelName()));
    }

    @Override
    public List<UUID> getMessagesUUIDFromChannel(UUID uuid){
        return channels.get(uuid).orElseThrow().getMessageList();
    }

    @Override
    public Optional<ChannelResponse> addMessageToChannel(UUID channelUUID, UUID messageUUID) {
        return channels.addMessageToChannel(channelUUID, messageUUID)
                .map(channel -> new ChannelResponse(channel.getChannelName()));
    }

    @Override
    public Optional<ChannelResponse> updateChannel(UUID uuid, String channelName) {
        return channels.updateName(uuid, channelName)
                .map(channel -> new ChannelResponse(channel.getChannelName()));
    }

    @Override
    public void deleteChannel(UUID uuid) {
        channels.remove(uuid);
        return;
    }

    @Override
    public List<ChannelResponse> getChannels() {
        return channels.getChannelsList().stream()
                .map(channel -> new ChannelResponse(channel.getChannelName()))
                .toList();
    }
}
