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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;
    private final CacheManager cacheManager;

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Transactional
    @Override
    public ChannelDto create(PublicChannelCreateRequest request) {
        log.debug("채널 생성 시작: {}", request);
        String name = request.name();
        String description = request.description();
        Channel channel = new Channel(ChannelType.PUBLIC, name, description);

        channelRepository.save(channel);

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    evictUserChannelsCache();
                    log.info("공개 채널 생성 완료: id={}, name={} - 트랜잭션 커밋 후 모든 사용자 채널 캐시 무효화",
                        channel.getId(), channel.getName());
                }
            }
        );

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

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    evictUserChannelsCache();
                    log.info("비공개 채널 생성 완료: id={} - 트랜잭션 커밋 후 참여자 {}명의 채널 캐시 무효화",
                        channel.getId(), request.participantIds().size());
                }
            }
        );

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

        try {
            List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId)
                .stream()
                .map(ReadStatus::getChannel)
                .map(Channel::getId)
                .toList();

            List<ChannelDto> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
                    mySubscribedChannelIds)
                .stream()
                .map(channelMapper::toDto)
                .toList();

            List<ChannelDto> result = new ArrayList<>(channels);

            log.info("사용자별 채널 목록 조회 완료: userId={}, 채널 수={} - DB 조회", userId, channels.size());
            return result;
        } catch (Exception e) {
            log.error("사용자별 채널 목록 조회 실패: userId={}", userId, e);
            return new ArrayList<>();
        }
    }

    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = CacheConfig.CHANNEL_DETAIL, key = "#channelId")
    })
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

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    evictChannelDetailCache(channelId);
                    evictUserChannelsCache();
                    log.info("채널 수정 완료: id={}, name={} - 트랜잭션 커밋 후 채널 상세 캐시 갱신, 모든 사용자 채널 캐시 무효화",
                        channelId, channel.getName());
                }
            }
        );

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

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    evictChannelDetailCache(channelId);
                    evictUserChannelsCache();
                    log.info("채널 삭제 완료: id={} - 트랜잭션 커밋 후 관련 캐시 무효화", channelId);
                }
            }
        );

        log.info("채널 삭제 완료: id={} - 관련 캐시 무효화", channelId);
    }

    private void evictUserChannelsCache() {
        try {
            org.springframework.cache.Cache cache = cacheManager.getCache(
                CacheConfig.USER_CHANNELS);
            if (cache != null) {
                cache.clear();
                log.debug("사용자 채널 캐시 무효화 완료");
            } else {
                log.warn("사용자 채널 캐시를 찾을 수 없음: cacheName={}", CacheConfig.USER_CHANNELS);
            }
        } catch (Exception e) {
            log.error("사용자 채널 캐시 무효화 실패", e);
        }
    }

    private void evictChannelDetailCache(UUID channelId) {
        try {
            org.springframework.cache.Cache cache = cacheManager.getCache(
                CacheConfig.CHANNEL_DETAIL);
            if (cache != null) {
                cache.evict(channelId);
                log.debug("채널 상세 캐시 무효화 완료: channelId={}", channelId);
            } else {
                log.warn("채널 상세 캐시를 찾을 수 없음: cacheName={}", CacheConfig.CHANNEL_DETAIL);
            }
        } catch (Exception e) {
            log.error("채널 상세 캐시 무효화 실패: channelId={}", channelId, e);
        }
    }
}
