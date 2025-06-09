package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.config.CacheConfig;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;


    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Transactional
    @CacheEvict(value = CacheConfig.USER_CHANNELS, allEntries = true)
    @Override
    public ChannelDto create(PublicChannelCreateRequest request) {
        log.debug("채널 생성 시작: {}", request);
        String name = request.name();
        String description = request.description();
        Channel channel = new Channel(ChannelType.PUBLIC, name, description);

        channelRepository.save(channel);
        log.info("채널 생성 완료: id={}, name={} - 모든 사용자 채널 캐시 무효화", channel.getId(), channel.getName());
        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest request) {
        log.debug("채널 생성 시작: {}", request);
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        channelRepository.save(channel);

        List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds())
            .stream()
            .map(user -> new ReadStatus(user, channel, channel.getCreatedAt(), true))
            .toList();
        readStatusRepository.saveAll(readStatuses);

        evictUserChannelsCacheForUsers(request.participantIds());

        log.info("채널 생성 완료: id={}, name={} - 참여자 {}의 채널 캐시 무효화", channel.getId(), channel.getName(),
            request.participantIds().size());
        return channelMapper.toDto(channel);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.CHANNEL_DETAIL, key = "#channelId")
    @Override
    public ChannelDto find(UUID channelId) {
        log.debug("채널 상세 조회: id={} - 캐시 확인", channelId);
        ChannelDto channelDto = channelRepository.findById(channelId)
            .map(channelMapper::toDto)
            .orElseThrow(() -> ChannelNotFoundException.withId(channelId));
        log.debug("채널 상세 조회 완료: id={} - DB 조회", channelId);
        return channelDto;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheConfig.USER_CHANNELS, key = "#userId")
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        log.debug("사용자별 채널 목록 조회: userId={} - 캐시 확인", userId);
        List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
            .map(ReadStatus::getChannel)
            .map(Channel::getId)
            .toList();

        List<ChannelDto> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
                mySubscribedChannelIds)
            .stream()
            .map(channelMapper::toDto)
            .toList();

        log.info("사용자별 채널 목록 조회 완료: userId={}, 채널 수={} - DB 조회", userId, channels.size());
        return channels;
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Transactional
    @CacheEvict(value = {CacheConfig.CHANNEL_DETAIL, CacheConfig.USER_CHANNELS},
        key = "#channelId", allEntries = true)
    @Override
    public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
        log.debug("채널 수정 시작: id={}, request={}", channelId, request);
        String newName = request.newName();
        String newDescription = request.newDescription();
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> ChannelNotFoundException.withId(channelId));
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            throw PrivateChannelUpdateException.forChannel(channelId);
        }
        channel.update(newName, newDescription);
        log.info("채널 수정 완료: id={}, name={} - 채널 상세 및 모든 사용자 채널 캐시 무효화", channelId,
            channel.getName());
        return channelMapper.toDto(channel);
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Transactional
    @Override
    public void delete(UUID channelId) {
        log.debug("채널 삭제 시작: id={}", channelId);
        if (!channelRepository.existsById(channelId)) {
            throw ChannelNotFoundException.withId(channelId);
        }

        List<UUID> participantIds = readStatusRepository.findAllByChannelIdWithUser(channelId)
            .stream()
            .map(readStatus -> readStatus.getUser().getId())
            .toList();

        messageRepository.deleteAllByChannelId(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);
        channelRepository.deleteById(channelId);

        evictChannelRelatedCaches(channelId, participantIds);

        log.info("채널 삭제 완료: id={} - 관련 캐시 무효화", channelId);
    }


    /**
     * 특정 사용자들의 채널 캐시 무효화
     */
    @CacheEvict(value = CacheConfig.USER_CHANNELS, allEntries = true)
    public void evictUserChannelsCacheForUsers(List<UUID> userIds) {
        log.debug("사용자들의 채널 캐시 무효화: userIds={}", userIds);
    }

    private void evictChannelRelatedCaches(UUID channelId, List<UUID> participantIds) {
        log.debug("채널 관련 캐시 무효화: channelId={}, participantIds={}", channelId, participantIds);
        evictUserChannelsCacheForUsers(participantIds);
    }
}
