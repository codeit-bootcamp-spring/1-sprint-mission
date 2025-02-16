package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreateChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "feature.service", havingValue = "file")
public class FileChannelService implements ChannelService {
    private final FileChannelRepository repository;

    @Override
    public ChannelResponse createChannel(CreateChannelRequest request) {
        Channel newChannel = new Channel(request.channelName());
        repository.save(newChannel);
        return new ChannelResponse(newChannel.getChannelName());
    }

    @Override
    public List<ChannelResponse> getChannels() {
        return repository.getAllChannels().stream()
                .map(channel -> new ChannelResponse(channel.getChannelName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ChannelResponse> getChannel(UUID id) {
        return repository.getChannelById(id)
                .map(channel -> new ChannelResponse(channel.getChannelName()));
    }

    @Override
    public List<UUID> getMessagesUUIDFromChannel(UUID channelId) {
        return repository.getChannelById(channelId)
                .map(Channel::getMessageList)
                .orElseThrow(() -> new RuntimeException("채널을 찾을 수 없습니다."));
    }

    @Override
    public Optional<ChannelResponse> addMessageToChannel(UUID channelId, UUID messageId) {
        return repository.getChannelById(channelId).map(channel -> {
            channel.addMessageToChannel(messageId);
            repository.save(channel);
            return new ChannelResponse(channel.getChannelName());
        });
    }

    @Override
    public Optional<ChannelResponse> updateChannel(UUID id, String newName) {
        return repository.getChannelById(id).map(channel -> {
            channel.updateChannelName(newName);
            repository.save(channel);
            return new ChannelResponse(channel.getChannelName());
        });
    }

    @Override
    public void deleteChannel(UUID id) {
        Optional<Channel> removedChannel = repository.getChannelById(id);
        removedChannel.ifPresent(channel -> repository.deleteById(id));
    }
}
