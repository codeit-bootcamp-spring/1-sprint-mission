package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channelService.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelService.ChannelDTO;
import com.sprint.mission.discodeit.dto.channelService.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channelService.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Qualifier("BasicChannelService")
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;


    @Override
    public Channel createPublicChannel(ChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PUBLIC, request.name(), request.description(), null);
        return channelRepository.save(channel);
    }
    @Override
    public Channel createPrivateChannel(PrivateChannelCreateRequest request) {
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Password is required for private channels.");
        }
        // 비공개 채널 생성
        Channel channel = new Channel(ChannelType.PRIVATE, request.name(), request.description(), request.password());

        // 채널을 먼저 저장
        channelRepository.save(channel);

        // 비공개 채널 참여자 등록
        List<UUID> participantIds = (request.userIds() != null) ? request.userIds() : List.of();

        for (UUID userId : participantIds) {
            if (!userRepository.existsById(userId)) {
                throw new IllegalArgumentException("User with id " + userId + " not found");
            }

            ReadStatus readStatus = new ReadStatus(userId, channel.getId(), null, null);
            readStatusRepository.save(readStatus);
        }

        return channel;
    }


    @Override
    public ChannelDTO find(UUID channelId) {
        //채널찾기
       Channel channel = channelRepository.findById(channelId)
               .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + "not found"));

       // 가장 최근 메시지 찾기
        Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(channelId);

        List<UUID> participantUserIds = channel.getType() == ChannelType.PRIVATE
                ? readStatusRepository.findUserIdsByChannelId(channelId) // 해당 채널에 참여한 유저 ID 리스트
                : List.of(); // PUBLIC 채널이면 빈 리스트 반환

        // DTO로 변환하여 반환
        return ChannelDTO.from(channel, lastMessageAt.orElse(null), participantUserIds);
          }

    @Override
    public List<ChannelDTO> findAllByUserId(UUID userId) {
        List<Channel> allChannels = channelRepository.findAll();

        return allChannels.stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC || readStatusRepository.existsByUserIdAndChannelId(userId, channel.getId())
                )
                .map(channel -> {
                    Optional<Instant> lastMessageAt = messageRepository.findLastMessageAtByChannelId(channel.getId());
                    List<UUID> participantUserIds =(channel.getType() == ChannelType.PRIVATE)
                            ? readStatusRepository.findByUserIdAndChannelId(userId, channel.getId())
                            .map(status -> List.of(status.getUserId()))
                            .orElse(List.of())
                            : List.of();
                    return ChannelDTO.from(channel, lastMessageAt.orElse(null), participantUserIds);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ChannelDTO update(ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + request.channelId() + " not found"));

        if(channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("Channel with id " + request.channelId() + " is private");
        }

        boolean updated = false;

        if(request.newName() != null && !request.newName().isBlank()) {
            channel.update(request.newName(), channel.getDescription());
            updated = true;
        }

        if(request.newDescription() != null && !request.newDescription().isBlank()) {
            channel.update(channel.getName(), channel.getDescription());
            updated = true;
        }

        if(updated) {
            channel = channelRepository.save(channel);
        }

        return ChannelDTO.from(channel, messageRepository.findLastMessageAtByChannelId(channel.getId()).orElse(null),
                channel.getType() == ChannelType.PRIVATE ? readStatusRepository.findUserIdsByChannelId(channel.getId()) : List.of());
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        messageRepository.deleteById(channelId);

        readStatusRepository.deleteByChannelId(channelId);

        channelRepository.deleteById(channelId);

    }

    @Override
    public Instant findLastMessageAtByChannelId(UUID channelId) {
        return messageRepository.findLastMessageAtByChannelId(channelId).orElse(null);
    }
}
