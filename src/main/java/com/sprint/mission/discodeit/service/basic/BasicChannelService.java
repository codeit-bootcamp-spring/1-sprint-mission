package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelValidator validator;

    private final MessageRepository messageRepository;
    private final ReadStatusService readStatusService;

    @Override
    public ChannelResponseDto create(ChannelCreateRequestDto channelCreateRequestDto) {
        if (channelCreateRequestDto.type() == ChannelType.PUBLIC) {
            return createPublicChannel(channelCreateRequestDto);
        }
        return createPrivateChannel(channelCreateRequestDto);
    }

    @Override
    public ChannelResponseDto createPublicChannel(ChannelCreateRequestDto channelCreateRequestDto) {
        validator.validate(channelCreateRequestDto.name(), channelCreateRequestDto.introduction());
        Channel channel = channelRepository.save(new Channel(ChannelType.PUBLIC, channelCreateRequestDto.name(),channelCreateRequestDto.introduction()));
        return getChannelInfo(channel, Instant.EPOCH);
    }

    @Override
    public ChannelResponseDto createPrivateChannel(ChannelCreateRequestDto channelCreateRequestDto) {
        Channel channel = channelRepository.save(new Channel(ChannelType.PRIVATE, channelCreateRequestDto.users()));
        channelCreateRequestDto.users()
                .forEach(user -> readStatusService.create(ReadStatusCreateDto.from(channel.getId(), user)));
        return getChannelInfo(channel, Instant.EPOCH);
    }

    @Override
    public ChannelResponseDto find(UUID channelId) {
        Channel channel = Optional.ofNullable(channelRepository.find(channelId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));

        return getChannelInfo(channel, findLastMessageTime(channelId));
    }

    @Override
    public List<ChannelResponseDto> findAll() {
        List<Channel> channels = channelRepository.findAll();

        return channels.stream()
                .map(channel -> getChannelInfo(channel,findLastMessageTime(channel.getId())))
                .toList();
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();

        return channels.stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC || channel.containsUser(userId))
                .map(channel -> getChannelInfo(channel,findLastMessageTime(channel.getId())))
                .toList();
    }

    @Override
    public Instant findLastMessageTime(UUID channelId) {
        return messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .max(Comparator.comparing(Message::getCreatedAt))
                .map(Message::getCreatedAt)
                .orElse(Instant.EPOCH);
    }

    @Override
    public ChannelResponseDto getChannelInfo(Channel channel, Instant lastMessageTime) {
        return ChannelResponseDto.from(channel.getId(), channel.getType(),
                channel.getName(), channel.getIntroduction(), lastMessageTime, channel.getParticipants());
    }

    @Override
    public ChannelResponseDto update(ChannelUpdateRequestDto channelUpdateRequestDto) {
        validator.validate(channelUpdateRequestDto.name(), channelUpdateRequestDto.introduction());
        Channel channel = Optional.ofNullable(channelRepository.find(channelUpdateRequestDto.id()))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));

        channel.update(channelUpdateRequestDto.name(), channelUpdateRequestDto.introduction());
        channelRepository.save(channel);

        return getChannelInfo(channel, findLastMessageTime(channel.getId()));
    }

    @Override
    public void delete(UUID channelId) {
        // TODO: 메시지 삭제
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다.");
        }
        readStatusService.deleteByChannelId(channelId);
        channelRepository.delete(channelId);
    }
}
