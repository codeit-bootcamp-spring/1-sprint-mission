package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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

    private final ReadStatusService readStatusService;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel create(PublicChannelCreateRequest channelCreateRequest) {
        Channel channel = new Channel(ChannelType.PUBLIC, channelCreateRequest.name(), channelCreateRequest.description());

        return channelRepository.save(channel);
    }

    @Override
    public Channel create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = channelRepository.save(new Channel(ChannelType.PRIVATE, null, null));

        privateChannelCreateRequest.participantsIds().stream()
                .map(userId -> ReadStatusCreateRequest.from(channel.getId(), userId))
                .forEach(readStatusService::create);

        return channel;
    }

    @Override
    public ChannelResponse find(UUID channelId) {
        Channel channel = Optional.ofNullable(channelRepository.find(channelId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));

        return getChannelInfo(channel, findLastMessageTime(channelId), findParticipantsIds(channel));
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
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
            readStatusRepository.findAllByChannelId(channel.getId())
                    .stream()
                    .map(ReadStatus::getUserId)
                    .forEach(participantIds::add);
        }
        return participantIds;
    }

    @Override
    public ChannelResponse getChannelInfo(Channel channel, Instant lastMessageTime, List<UUID> participantIds) {
        return ChannelResponse.from(channel.getId(), channel.getType(),
                channel.getName(), channel.getDescription(), lastMessageTime, participantIds);
    }

    @Override
    public Channel update(UUID channelId, PublicChannelUpdateRequest channelUpdateRequest) {
        validator.validate(channelUpdateRequest.name(), channelUpdateRequest.description());
        Channel channel = Optional.ofNullable(channelRepository.find(channelId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));

        channel.update(channelUpdateRequest.name(), channelUpdateRequest.description());
        return channelRepository.save(channel);
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
