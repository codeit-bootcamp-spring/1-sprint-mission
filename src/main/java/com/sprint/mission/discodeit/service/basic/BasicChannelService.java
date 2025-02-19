package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ChannelResponse createPrivateChannel(PrivateChannelCreateRequest request) {
        List<User> users = userRepository.findAllById(request.getUserIds());
        if (users.isEmpty()) {
            throw new IllegalArgumentException("At least one user must be included.");
        }

        Channel channel = new Channel(ChannelType.PRIVATE, request.getName(), request.getDescription());
        channel = channelRepository.save(channel);

        for (User user : users) {
            ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());
            readStatusRepository.save(readStatus);
        }

        return new ChannelResponse(channel, null, users.stream().map(User::getId).collect(Collectors.toList()));
    }

    @Override
    public ChannelResponse createPublicChannel(PublicChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PUBLIC, request.getName(), request.getDescription());
        channel = channelRepository.save(channel);
        return new ChannelResponse(channel, null, null);
    }

    @Override
    public ChannelResponse find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));

        Instant lastMessageTime = messageRepository.findLastMessageTimeByChannelId(channelId);
        List<UUID> participants = channel.getType() == ChannelType.PRIVATE
                ? readStatusRepository.findUserIdsByChannelId(channelId)
                : null;

        return new ChannelResponse(channel, lastMessageTime, participants);
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        List<Channel> publicChannels = channelRepository.findByType(ChannelType.PUBLIC);
        List<Channel> privateChannels = channelRepository.findByUserId(userId);

        List<ChannelResponse> responses = new ArrayList<>();
        for (Channel channel : publicChannels) {
            Instant lastMessageTime = messageRepository.findLastMessageTimeByChannelId(channel.getId());
            responses.add(new ChannelResponse(channel, lastMessageTime, null));
        }
        for (Channel channel : privateChannels) {
            Instant lastMessageTime = messageRepository.findLastMessageTimeByChannelId(channel.getId());
            List<UUID> participants = readStatusRepository.findUserIdsByChannelId(channel.getId());
            responses.add(new ChannelResponse(channel, lastMessageTime, participants));
        }
        return responses;
    }

    @Override
    public ChannelResponse update(ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("PRIVATE channels cannot be updated.");
        }

        channel.update(request.getName(), request.getDescription());
        channel = channelRepository.save(channel);
        return new ChannelResponse(channel, null, null);
    }


    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));

        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);
        channelRepository.delete(channel);
    }
}
