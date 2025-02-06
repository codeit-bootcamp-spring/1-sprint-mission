package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.Channel.ChannelDto;
import com.sprint.mission.discodeit.dto.Channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public Channel create(ChannelCreateRequest request) {
        Channel channel = new Channel(request.name(), request.description(), request.channelType());
        if (channel.getChannelType() == ChannelType.PRIVATE){
            for (UUID userId : request.participantIds()) {
                channel.addParticipant(userId);
                readStatusRepository.save(new ReadStatus(userId, channel.getId()));
            }
        }

        return channelRepository.save(channel);
    }

    @Override
    public ChannelDto findById(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);
        Instant lastReadTime = readStatusRepository.findByChannelId(channelId).getLastReadTime();
        return new ChannelDto(channel.getId(), channel.getName(), channel.getDescription(), channel.getChannelType(), channel.getCreatedAt(), channel.getUpdatedAt(), new ArrayList<>(channel.getParticipants()), lastReadTime);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        return channels.stream()
                .filter(channel -> channel.getChannelType() == ChannelType.PUBLIC || channel.getParticipants().contains(userId))
                .map(channel -> {
                    Instant lastReadTime = readStatusRepository.findByUserId(userId).getLastReadTime();
                    return new ChannelDto(channel.getId(), channel.getName(), channel.getDescription(), channel.getChannelType(), channel.getCreatedAt(), channel.getUpdatedAt(), new ArrayList<>(channel.getParticipants()), lastReadTime);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {
        // 채널 정보 조회
        Channel channel = channelRepository.findById(channelId);

        // Channel 엔티티의 update 메서드를 호출해서 정보를 업데이트
        channel.update(request.name(), request.description(), request.channelType());

        // 마지막 읽은 시간 조회
        Instant lastReadTime = readStatusRepository.findByChannelId(channelId).getLastReadTime();

        // ChannelDto 반환
        return new ChannelDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getChannelType(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                new ArrayList<>(channel.getParticipants()),
                lastReadTime
        );
    }

    @Override
    public void delete(UUID channelId) {
        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);
        channelRepository.deleteByChannelId(channelId);
    }
}
