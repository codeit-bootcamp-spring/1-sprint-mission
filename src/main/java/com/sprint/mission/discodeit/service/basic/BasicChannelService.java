package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelValidator validator;

    private final ReadStatusService readStatusService;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public Channel create(PublicChannelCreateRequest channelCreateRequest) {
        Channel channel = new Channel(ChannelType.PUBLIC, channelCreateRequest.name(), channelCreateRequest.description());

        return channelRepository.save(channel);
    }

    @Override
    @Transactional
    public Channel create(PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = channelRepository.save(new Channel(ChannelType.PRIVATE, null, null));

        privateChannelCreateRequest.participantsIds().stream()
                .map(userId -> ReadStatusCreateRequest.from(channel.getId(), userId, Instant.MIN))
                .forEach(readStatusService::create);

        return channel;
    }

    @Override
    public ChannelResponse find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));

        return getChannelInfo(channel, findLastMessageTime(channelId), findParticipantsIds(channel));
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        List<UUID> joinedChannels = readStatusService.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannel)
                .map(BaseEntity::getId)
                .toList();

        return channelRepository.findAll().stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC || joinedChannels.contains(channel.getId()))
                .map(channel -> getChannelInfo(channel, findLastMessageTime(channel.getId()), findParticipantsIds(channel)))
                .toList();
    }

    @Override
    public Instant findLastMessageTime(UUID channelId) {
        return messageRepository.findAll().stream()
                .filter(message -> message.isSameChannelById(channelId))
                .max(Comparator.comparing(Message::getCreatedAt))
                .map(Message::getCreatedAt)
                .orElse(Instant.EPOCH);
    }

    @Override
    public List<UUID> findParticipantsIds(Channel channel) {
        List<UUID> participantIds = new ArrayList<>();
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            readStatusRepository.findByChannelId(channel.getId())
                    .stream()
                    .map(ReadStatus::getUser)
                    .map(BaseEntity::getId)
                    .forEach(participantIds::add);
        }
        return participantIds;
    }

    @Override
    public ChannelResponse getChannelInfo(Channel channel, Instant lastMessageAt, List<UUID> participantIds) {
        return ChannelResponse.from(channel.getId(), channel.getType(),
                channel.getName(), channel.getDescription(), lastMessageAt, participantIds);
    }

    @Override
    @Transactional
    public Channel update(UUID channelId, PublicChannelUpdateRequest channelUpdateRequest) {
        validator.validate(channelUpdateRequest.newName(), channelUpdateRequest.newDescription());
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));

        channel.update(channelUpdateRequest.newName(), channelUpdateRequest.newDescription());
        return channelRepository.save(channel);
    }

    @Override
    @Transactional
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다.");
        }
        channelRepository.deleteById(channelId);
    }
}
