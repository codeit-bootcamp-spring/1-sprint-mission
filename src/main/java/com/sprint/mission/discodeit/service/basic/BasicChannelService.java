package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
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
    public Channel create(PublicChannelCreateRequest channelCreateRequestDto) {
        Channel channel = new Channel(ChannelType.PUBLIC, channelCreateRequestDto.name(), channelCreateRequestDto.introduction());

        return channelRepository.save(channel);
    }

    @Override
    public Channel create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);

        return channelRepository.save(channel);
    }

    @Override
    public ChannelResponseDto find(UUID channelId) {
        Channel channel = Optional.ofNullable(channelRepository.find(channelId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));

        return getChannelInfo(channel, findLastMessageTime(channelId), findParticipantsIds(channel));
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<UUID> joinedChannels = readStatusService.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .toList();

        return channelRepository.findAll().stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC || joinedChannels.contains(channel.getId()))
                .map(channel -> getChannelInfo(channel, findLastMessageTime(channel.getId()), findParticipantsIds(channel)))
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
    public List<UUID> findParticipantsIds(Channel channel) {
        List<UUID> participantIds = new ArrayList<>();
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            readStatusService.findAllByUserId(channel.getId())
                    .stream()
                    .map(ReadStatus::getUserId)
                    .forEach(participantIds::add);
        }
        return participantIds;
    }

    @Override
    public ChannelResponseDto getChannelInfo(Channel channel, Instant lastMessageTime, List<UUID> participantIds) {


        return ChannelResponseDto.from(channel.getId(), channel.getType(),
                channel.getName(), channel.getIntroduction(), lastMessageTime, participantIds);
    }

    @Override
    public ChannelResponseDto update(PublicChannelUpdateRequest channelUpdateRequestDto) {
        validator.validate(channelUpdateRequestDto.name(), channelUpdateRequestDto.introduction());
        Channel channel = Optional.ofNullable(channelRepository.find(channelUpdateRequestDto.id()))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));

        channel.update(channelUpdateRequestDto.name(), channelUpdateRequestDto.introduction());
        channelRepository.save(channel);

        return getChannelInfo(channel, findLastMessageTime(channel.getId()));
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다.");
        }
        messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .forEach(message -> messageRepository.delete(message.getId()));
        readStatusService.deleteByChannelId(channelId);
        channelRepository.delete(channelId);
    }
}
