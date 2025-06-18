package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ChannelRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;
    private final ReadStatusService readStatusService;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    @CacheEvict(cacheNames = "channels", allEntries = true)
    @Override
    @Transactional
    public ChannelResponse createPublicChannel(ChannelRequest.CreatePublic request) {
        Channel newChannel = Channel.createChannel(Channel.ChannelType.PUBLIC, request.getName(),
            request.getDescription());
        channelRepository.save(newChannel);

        log.info("Created public channel - id: {}", newChannel.getId());
        return channelMapper.entityToDto(newChannel);
    }

    @Override
    @Transactional
    public ChannelResponse createPrivateChannel(ChannelRequest.CreatePrivate request) {
        Channel newChannel = Channel.createChannel(Channel.ChannelType.PRIVATE, null, null);
        channelRepository.save(newChannel);

        for (UUID userId : request.getParticipantIds()) {
            readStatusService.create(userId,
                new ReadStatusRequest.Create(userId, newChannel.getId(),
                    newChannel.getCreatedAt()));

            // 채팅 참여자에 한해 캐시 무효화
            Objects.requireNonNull(cacheManager.getCache("channels")).evict(userId);
        }

        log.info("Created private channel - id: {}", newChannel.getId());
        return channelMapper.entityToDto(newChannel);
    }

    @Cacheable(cacheNames = "channels", key = "#userId", unless = "#result.isEmpty()")
    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) { // N + 1;
        User user = userRepository.findById(userId).orElseThrow(() ->
            new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)));

        return channelRepository.findAllByUserIdOrType(userId, ChannelType.PUBLIC).stream()
            .map(channelMapper::entityToDto)
            .collect(Collectors.toList());
    }

    @Override
    public ChannelResponse findById(UUID id) {
        return channelMapper.entityToDto(findByIdOrThrow(id));
    }

    @CacheEvict(cacheNames = "channels", allEntries = true)
    @Override
    @Transactional
    public ChannelResponse update(UUID id, ChannelRequest.Update request) {
        Channel channel = findByIdOrThrow(id);

        if (channel.getType() == Channel.ChannelType.PRIVATE) {
            throw new PrivateChannelUpdateException(ErrorCode.PRIVATE_CHANNEL_CANNOT_BE_MODIFIED,
                Map.of("id", id));
        }

        Optional.ofNullable(request.getName()).ifPresent(channel::updateName); // TODO : 같은지 확인
        Optional.ofNullable(request.getDescription()).ifPresent(channel::updateDescription);

        log.info("Updated public channel - id: {}", channel.getId());
        return channelMapper.entityToDto(channel);
    }

    @Override
    public void deleteById(UUID id) {
        findByIdOrThrow(id);
        channelRepository.deleteById(id);
        log.info("Deleted channel - id: {}", id);
    }

    private Channel findByIdOrThrow(UUID id) {
        return channelRepository.findById(id)
            .orElseThrow(
                () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, Map.of("id", id)));
    }

}
