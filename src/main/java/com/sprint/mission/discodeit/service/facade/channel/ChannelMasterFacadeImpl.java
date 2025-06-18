package com.sprint.mission.discodeit.service.facade.channel;


import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.channel.ChannelManagementService;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.message.MessageService;
import com.sprint.mission.discodeit.service.user.UserService;
import com.sprint.mission.discodeit.util.CacheUtil;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMasterFacadeImpl implements ChannelMasterFacade {

  private final ChannelManagementService channelManagementService;

  private final MessageService messageService;
  private final ChannelService channelService;
  private final ChannelMapper channelMapper;
  private final UserService userService;

  private final CacheManager manager;
  private final ReadStatusService readStatusService;

  @Override
  @Transactional
  public ChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto) {

    Channel channel = channelManagementService.createPrivateChannel(
        channelMapper.toEntity(channelDto),
        channelDto.participantIds()
    );

    log.debug("[CREATED PRIVATE CHANNEL AND READ STATUSES] : [USER_IDS : {}]",
        channelDto.participantIds());

    List<User> participants = channel.getStatuses().stream()
        .map(ReadStatus::getUser).toList();

    CacheUtil.evictCache(manager, channelDto.participantIds(), "userChannelList");

    return channelMapper.toDto(channel, Instant.EPOCH, participants);
  }

  @Override
  @Transactional
  @CacheEvict(cacheNames = "userChannelList", allEntries = true)
  public ChannelResponseDto createPublicChannel(CreateChannelDto channelDto) {

    Channel channel = channelManagementService.createPublicChannel(
        channelMapper.toEntity(channelDto));

    log.debug("[CREATED PUBLIC CHANNEL] : [ID: {}][NAME: {}]", channel.getId(), channel.getName());

    return channelMapper.toDto(channel, Instant.EPOCH, Collections.emptyList());
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelResponseDto getChannelById(String channelId) {
    Channel channel = channelService.findChannelById(channelId);

    List<User> participants = channel.getStatuses().stream()
        .map(ReadStatus::getUser)
        .toList();

    Instant lastMessageTime = channelManagementService.getLastMessageTimeForChannel(channelId);

    return channelMapper.toDto(channel, lastMessageTime, participants);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(cacheNames = "userChannelList", key = "#userId", unless = "#result == null")
  public List<ChannelResponseDto> findAllChannelsByUserIdV3(String userId) {
    List<Channel> channels = channelManagementService.findAllChannelsForUser(userId);

    Map<UUID, Instant> latestMessageTimeByChannel = messageService.getLatestMessageForChannels(
        channels);

    Map<UUID, List<User>> channelParticipants = channelMapper.channelToParticipants(channels);

    return channelMapper.toListResponse(channels, latestMessageTimeByChannel, channelParticipants);
  }

  @Override
  @Transactional
  public ChannelResponseDto updateChannel(String channelId, ChannelUpdateDto channelUpdateDto) {

    Channel channel = channelService.updateChannel(channelId, channelUpdateDto);
    log.debug("[UPDATED CHANNEL] : [ID: {}]", channelId);

    List<ReadStatus> statuses = readStatusService.findAllByChannelId(channelId);
    log.debug("[FOUND READ STATUSES] : [CHANNEL_ID: {}]", channelId);

    List<UUID> userIds = statuses.stream().map(status -> status.getUser().getId())
        .collect(Collectors.toList());

    List<User> users = userService.findByAllIn(userIds);
    log.debug("[FOUND USERS] : [USER_IDS : {}]", userIds);

    Instant lastMessageTime = channelManagementService
        .getLastMessageTimeForChannel(channelId);

    log.debug("[FOUND LATEST MESSAGE TIME] : [TIME : {}]", lastMessageTime);
    return channelMapper.toDto(channel, lastMessageTime, users);
  }

  @Override
  @Transactional
  public void deleteChannel(String channelId) {

    if (channelId.isBlank()) {
      throw new DiscodeitException(ErrorCode.INVALID_UUID_FORMAT, Map.of("channelId", channelId));
    }

    List<ReadStatus> statuses = readStatusService.findAllByChannelId(channelId);
    List<String> userIds = statuses.stream()
        .map(status -> status.getUser().getId().toString())
        .toList();

    CacheUtil.evictCache(manager, userIds, "userChannelList");

    channelService.deleteChannel(channelId);
  }

}
