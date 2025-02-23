package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.PublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelInfoDto;
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
        Channel channel = Channel.of(ChannelType.PRIVATE, user.getName(), user.getName() + "의 Private 채널");
        channel.addUser(user);
        ReadStatus readStatus = ReadStatus.of(user.getId(), channel.getId());
        readStatusRepository.save(readStatus);
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPublicChannel(PublicChannelDto publicChannelDto) {
        return channelRepository.save(Channel.of(ChannelType.PUBLIC, publicChannelDto.name(), publicChannelDto.description()));
    }

    @Override
    public ChannelInfoDto readChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
        List<Message> messages = messageRepository.findByChannelId(channelId);
        Instant latestMessageTime = messages.stream()
                .max(Comparator.comparing(Message::getCreatedAt))
                .map(Message::getCreatedAt)
                .orElse(channel.getCreatedAt());

        List<UUID> userIdList = channel.getUsers().values().stream()
                .map(User::getId)
                .toList();

        return ChannelInfoDto.of(channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
                channel.getType(), channel.getName(), channel.getDescription(), latestMessageTime, userIdList);
    }

    @Override
    public List<ChannelInfoDto> readAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAllByUserId(userId);
        List<ChannelInfoDto> channelInfoDtos = new ArrayList<>(100);

        for (Channel channel : channels) {
            List<Message> messages = messageRepository.findByChannelId(channel.getId());
            Instant latestMessageTime = messages.stream()
                    .max(Comparator.comparing(Message::getCreatedAt))
                    .map(Message::getCreatedAt)
                    .orElse(channel.getCreatedAt());
            List<UUID> userIdList = channel.getUsers().values().stream()
                    .map(User::getId)
                    .toList();
            channelInfoDtos.add(ChannelInfoDto.of(channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
                    channel.getType(), channel.getName(), channel.getDescription(), latestMessageTime, userIdList));
        }

        return channelInfoDtos;
    }

    @Override
    public void updateChannel(UUID channelId, PublicChannelDto publicChannelDto) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundException("등록되지 않은 channel. id=" + channelId));
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new PrivateChannelModificationException("private 채널은 수정할 수 없습니다.");
        }

        channel.updateName(publicChannelDto.name());
        channel.updateDescription(publicChannelDto.description());
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