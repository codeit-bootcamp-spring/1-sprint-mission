package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
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

    @Override
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        channelRepository.save(channel);
        for (UUID userId : request.participants()) {
            channel.addParticipant(userId);
            readStatusRepository.save(new ReadStatus(userId, channel.getId()));
        }
        return changeToDto(Optional.of(channel), Instant.now());
    }

    @Override
    public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
        Channel channel = new Channel(request.name(), request.description(), ChannelType.PUBLIC);
        channelRepository.save(channel); // 모든 사용자 조회
        for (UUID userId : request.participants()) {
            channel.addParticipant(userId);
            readStatusRepository.save(new ReadStatus(userId, channel.getId()));
        }
        return changeToDto(Optional.of(channel), Instant.now());
    }

    @Override
    public ChannelDto findById(UUID channelId) {
        Optional<Channel> channel = channelRepository.findById(channelId);
        Instant lastReadTime = readStatusRepository.findByChannelId(channelId)
                .map(ReadStatus::getLastReadTime)
                .orElse(Instant.now());
        return changeToDto(channel, lastReadTime);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        return channels.stream()
                .filter(channel -> channel.getChannelType() == ChannelType.PUBLIC || channel.getParticipants().contains(userId))
                .map(channel -> {
                    Instant lastReadTime = readStatusRepository.findByUserId(userId)
                            .map(ReadStatus::getLastReadTime)
                            .orElse(Instant.now());
                    return changeToDto(Optional.of(channel), lastReadTime);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ChannelDto update(ChannelUpdateRequest request) {
        // 채널 정보 조회
        Optional<Channel> channel = channelRepository.findById(request.id());
        // PRIVATE 채널은 수정할 수 없습니다.
        if (channel.get().getChannelType() == ChannelType.PRIVATE) {
            throw new UnsupportedOperationException("PRIVATE 채널은 수정할 수 없습니다.");
        }
        Optional<ReadStatus> existingReadStatuses = readStatusRepository.findByChannelId(request.id());
        Set<UUID> existingUserIds = existingReadStatuses.stream()
                .map(ReadStatus::getUserId)
                .collect(Collectors.toSet());

        // 새로 추가된 사용자에 대한 ReadStatus 저장
        for (UUID userId : request.participants()) {
            if (!existingUserIds.contains(userId)) { // 기존 사용자가 아닌 경우만 추가
                channel.get().addParticipant(userId);
                readStatusRepository.save(new ReadStatus(userId, channel.get().getId()));
            }
        }
        // Channel 엔티티 업데이트
        channel.get().update(request.name(), request.description(), request.channelType());

        // 마지막 읽은 시간 조회 (모든 참여자 중 가장 오래된 시간)
        Instant lastReadTime = existingReadStatuses.stream()
                .map(ReadStatus::getLastReadTime)
                .min(Instant::compareTo) // 가장 오래된 lastReadTime을 가져옴
                .orElse(Instant.now());

        // ChannelDto 반환
        return changeToDto(channel, lastReadTime);
    }

    @Override
    public void delete(UUID channelId) {
        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);
        channelRepository.deleteByChannelId(channelId);
    }

    private static ChannelDto changeToDto(Optional<Channel> channel, Instant lastReadTime) {
        return new ChannelDto(
                channel.get().getId(),
                channel.get().getName(),
                channel.get().getDescription(),
                channel.get().getChannelType(),
                channel.get().getCreatedAt(),
                channel.get().getUpdatedAt(),
                new ArrayList<>(channel.get().getParticipants()),
                lastReadTime
        );
    }
}
