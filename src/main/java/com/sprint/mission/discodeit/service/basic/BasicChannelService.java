package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.response.ChannelDetailResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.exception.PrivateChannelModificationException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public Channel createPrivateChannel(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
        Channel channel = Channel.of(Channel.Type.PRIVATE, user.getName(), user.getName() + "의 Private 채널");
        channel.addUser(user);
        ReadStatus readStatus = ReadStatus.of(user.getId(), channel.getId());
        readStatusRepository.save(readStatus);
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPublicChannel(PublicChannelRequest publicChannelRequest) {
        return channelRepository.save(Channel.of(Channel.Type.PUBLIC, publicChannelRequest.name(), publicChannelRequest.description()));
    }

    @Override
    public ChannelDetailResponse readChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
        List<Message> messages = messageRepository.findByChannelId(channelId);
        Instant latestMessageTime = messages.stream()
                .max(Comparator.comparing(Message::getCreatedAt))
                .map(Message::getCreatedAt)
                .orElse(channel.getCreatedAt());

        return ChannelDetailResponse.of(channel, latestMessageTime);
    }

    @Override
    public List<ChannelDetailResponse> readAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAllByUserId(userId);
        List<ChannelDetailResponse> channelDetailResponses = new ArrayList<>(100);

        for (Channel channel : channels) {
            List<Message> messages = messageRepository.findByChannelId(channel.getId());
            Instant latestMessageTime = messages.stream()
                    .max(Comparator.comparing(Message::getCreatedAt))
                    .map(Message::getCreatedAt)
                    .orElse(channel.getCreatedAt());

            channelDetailResponses.add(ChannelDetailResponse.of(channel, latestMessageTime));
        }

        return channelDetailResponses;
    }

    @Override
    public void updateChannel(UUID channelId, PublicChannelRequest publicChannelRequest) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
        if (channel.getType() == Channel.Type.PRIVATE) {
            throw new PrivateChannelModificationException("private 채널은 수정할 수 없습니다.");
        }

        channel.updateName(publicChannelRequest.name());
        channel.updateDescription(publicChannelRequest.description());
        channelRepository.updateChannel(channel);
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 user. id=" + userId));
        channel.addUser(user);
        channelRepository.updateChannel(channel);
    }

    @Override
    public void deleteUser(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
        channel.deleteUser(userId);
        channelRepository.updateChannel(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);
        channelRepository.deleteChannel(channelId);
    }
}