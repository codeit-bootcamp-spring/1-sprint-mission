package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.channel.ChannelDto;
import com.spirnt.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.Channel;
import com.spirnt.mission.discodeit.entity.ChannelType;
import com.spirnt.mission.discodeit.entity.Message;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.spirnt.mission.discodeit.exception.Channel.PrivateChannelUpdateException;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.mapper.ChannelMapper;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.ReadStatusRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {

    private final ChannelMapper channelMapper;
    private final UserMapper userMapper;

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final ReadStatusService readStatusService;
    private final ReadStatusRepository readStatusRepository;

    private final SessionRegistry sessionRegistry;

    @Transactional
    @Override
    public ChannelDto createChannelPublic(PublicChannelCreateRequest publicChannelCreateRequest) {
        Channel channel = channelRepository.save(new Channel(publicChannelCreateRequest.name(),
            publicChannelCreateRequest.description(), ChannelType.PUBLIC));
        return channelMapper.toDto(channel,
            getParticipants(channel),
            getLastMessageAt(channel.getId()).orElse(channel.getCreatedAt()));
    }

    @Transactional
    @Override
    public ChannelDto createChannelPrivate(
        PrivateChannelCreateRequest privateChannelCreateRequest) {
        Channel channel = channelRepository.save(new Channel(null, null, ChannelType.PRIVATE));
        // 채널에 참여하는 유저별 ReadStatus 생성
        List<UUID> participantIds = privateChannelCreateRequest.participantIds();
        for (UUID userId : participantIds) {
            readStatusService.create(
                new ReadStatusCreateRequest(userId, channel.getId(), channel.getCreatedAt()));
        }
        return channelMapper.toDto(channel,
            getParticipants(channel),
            getLastMessageAt(channel.getId()).orElse(channel.getCreatedAt()));
    }

    @Override
    public ChannelDto find(UUID userId, UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(
            () -> new ChannelNotFoundException(Map.of("channelId", channelId)));
        return channelMapper.toDto(channel,
            getParticipants(channel),
            getLastMessageAt(channel.getId()).orElse(channel.getCreatedAt()));
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        // User가 존재하지 않으면 예외 발생
        if (!userRepository.existsById(userId)) {
            log.warn("[Finding All Channels By UserId Failed: User with id {} not found]", userId);
            throw new UserNotFoundException(Map.of("userId", userId));
        }
        // PUBLIC인 채널은 모두 가져옴
        List<Channel> channels = channelRepository.findAllPublic();
        // ReadStatusRepository에서 유저가 참여한 private 채널 모두 가져오기
        List<Channel> privateChannels = readStatusRepository.findAllByUserId(userId).stream()
            .map(readStatus -> readStatus.getChannel())
            .filter(channel -> channel.getType().equals(ChannelType.PRIVATE))
            .collect(Collectors.toList());

        // PUBLIC + PRIVATE 채널을 하나의 리스트로 병합
        channels.addAll(privateChannels);

        return channels.stream()
            .sorted(Comparator.comparing(channel -> channel.getCreatedAt()))
            .map(channel -> channelMapper.toDto(channel,
                getParticipants(channel),
                getLastMessageAt(channel.getId()).orElse(channel.getCreatedAt())))
            .toList();
    }

    @Transactional
    @Override
    public ChannelDto update(UUID channelId,
        PublicChannelUpdateRequest publicChannelUpdateRequest) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> {
            log.warn("[Updating Channel Failed: Channel with id {} not found]", channelId);
            return new ChannelNotFoundException(Map.of("channelId", channelId));
        });
        // PRIVATE 채널은 업데이트할 수 없음
        if (channel.getType() == ChannelType.PRIVATE) {
            log.warn("[Updating Channel Failed: Private Channel(id: {}) cannot be updated]",
                channelId);
            throw new PrivateChannelUpdateException(Map.of("channelId", channelId));
        }
        channel.update(publicChannelUpdateRequest.newName(),
            publicChannelUpdateRequest.newDescription());
        // 새롭게 업데이트 된 채널로 dto 반환
        return channelMapper.toDto(channel,
            getParticipants(channel),
            getLastMessageAt(channel.getId()).orElse(channel.getCreatedAt()));
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> {
            log.warn("[Deleting Channel Failed: Channel with id {} not found]", channelId);
            return new ChannelNotFoundException(Map.of("channelId", channelId));
        });
        channelRepository.delete(channel);
    }

    // 해당 채널 메세지를 정렬하여 가장 최근 메세지 시간 찾기
    public Optional<Instant> getLastMessageAt(UUID channelId) {
        Optional<Instant> lastSeenAt = messageRepository.findAllByChannelId(channelId).stream()
            .map(Message::getCreatedAt)
            .findFirst();
        return lastSeenAt;
    }

    public List<UserDto> getParticipants(Channel channel) {
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            return readStatusRepository.findAllByChannelId(channel.getId()).stream()
                .map(readStatus -> toUserDto(readStatus.getUser()))
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<Channel> findPrivateChannelsByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId)
            .stream().map(readStatus -> readStatus.getChannel())
            .filter(channel -> channel.getType().equals(ChannelType.PRIVATE))
            .collect(Collectors.toList());
    }

    public boolean isUserOnline(String username) {
        return sessionRegistry.getAllPrincipals().stream()
            .filter(principal -> principal instanceof UserDetails)
            .map(principal -> (UserDetails) principal)
            .anyMatch(userDetails -> userDetails.getUsername().equals(username));
    }

    public UserDto toUserDto(User user) {
        return userMapper.toDto(user, isUserOnline(user.getUsername()));
    }
}


