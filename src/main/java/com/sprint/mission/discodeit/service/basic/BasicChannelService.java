package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelInfoDto;
import com.sprint.mission.discodeit.entity.*;
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
        User user = userRepository.findById(userId);
        Channel channel = Channel.of(ChannelType.PRIVATE, user.getName(), user.getName() + "의 Private 채널");
        channel.addUser(user);
        ReadStatus readStatus = ReadStatus.of(user.getId(), channel.getId());
        readStatusRepository.save(readStatus);
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPublicChannel(ChannelDto channelDto) {
        return channelRepository.save(Channel.of(ChannelType.PUBLIC, channelDto.name(), channelDto.description()));
    }

    @Override
    public ChannelInfoDto readChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);
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
            Optional<Message> message = messages.stream()
                    .max(Comparator.comparing(Message::getCreatedAt));
            List<UUID> userIdList = channel.getUsers().values().stream()
                    .map(User::getId)
                    .toList();
            channelInfoDtos.add(ChannelInfoDto.of(channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
                    channel.getType(), channel.getName(), channel.getDescription(), message.isEmpty() ? channel.getCreatedAt() : message.get().getCreatedAt(), userIdList));
        }

        return channelInfoDtos;
    }

    @Override
    public void updateChannel(UUID channelId, ChannelDto channelDto) {
        Channel channel = channelRepository.findById(channelId);
        if (channel.getType() == ChannelType.PRIVATE) {
            throw new UnsupportedOperationException("private 채널은 수정할 수 없습니다.");
        }

        channel.updateName(channelDto.name());
        channel.updateDescription(channelDto.description());
        channelRepository.updateChannel(channel);
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId);
        channel.addUser(userRepository.findById(userId));
        channelRepository.updateChannel(channel);
    }

    @Override
    public void deleteUser(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId);
        channel.deleteUser(userId);
        channelRepository.updateChannel(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelRepository.deleteChannel(channelId);
    }
}