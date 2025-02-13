package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validation.ValidateChannel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ValidateChannel validateChannel;

    @Override
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        Channel createdChannel = channelRepository.save(channel);
        for (UUID userId : request.participants()) {
            readStatusRepository.save(new ReadStatus(userId, createdChannel.getId(), Instant.MIN));
        }
        return changeToDto(createdChannel);
    }

    @Override
    public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
        // validateChannel.validatePublicChannel(request.name(), request.description());

        Channel channel = new Channel(request.name(), request.description(), ChannelType.PUBLIC);
        Channel createdChannel = channelRepository.save(channel); // 모든 사용자 조회

        return changeToDto(createdChannel);
    }

    @Override
    public ChannelDto findById(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(this::changeToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found."));
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<UUID> channelIds = readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .toList();
        return channelRepository.findAll().stream()
                .filter(channel -> channel.getChannelType().equals(ChannelType.PUBLIC) || channelIds.contains(channel.getId()))
                .map(this::changeToDto)
                .toList();
    }

    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
//        validateChannel.validatePublicChannelType(request.channelType());
//        validateChannel.validatePublicChannel(request.name(), request.description());
        String newName = request.newName();
        String newDescription = request.newDescription();
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ResourceNotFoundException("Channel not found."));

        channel.update(newName, newDescription);
        Channel updatedChannel = channelRepository.save(channel);
        return changeToDto(updatedChannel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        messageRepository.deleteAllByChannelId(channel.getId());
        readStatusRepository.deleteAllByChannelId(channel.getId());

        channelRepository.deleteById(channelId);
    }

    private ChannelDto changeToDto(Channel channel) {
        // 가장 늦게 생성된 메시지 시간
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst()
                .orElse(Instant.MIN);

        // Private 채널일 경우 참여자 넣기
        List<UUID> participantIds = new ArrayList<>();
        if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
            readStatusRepository.findAllByChannelId(channel.getId()).stream()
                    .map(ReadStatus::getUserId)
                    .forEach(participantIds::add);
        }

        return new ChannelDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getChannelType(),
                participantIds,
                lastMessageAt,
                channel.getCreatedAt(),
                channel.getUpdatedAt()
        );
    }
}
