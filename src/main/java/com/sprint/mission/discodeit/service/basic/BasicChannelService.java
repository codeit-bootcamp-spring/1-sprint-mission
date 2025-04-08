package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ChannelMapper channelMapper;

  private final ReadStatusService readStatusService;
  private final ReadStatusRepository readStatusRepository;

  @Override
  @Transactional
  public ChannelDto create(PublicChannelCreateRequest request) {
    Channel channel = new Channel(Channel.ChannelType.PUBLIC, request.name(), request.description());
    Channel savedChannel = channelRepository.save(channel);
    log.info("Channel(public) entity saved: id = {}", savedChannel.getId());

    return channelMapper.toDto(savedChannel);
  }

  @Override
  @Transactional
  public ChannelDto create(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(Channel.ChannelType.PRIVATE, null, null);
    Channel savedChannel = channelRepository.save(channel);
    log.info("Channel(private) entity saved: id = {}", savedChannel.getId());

    request.participantsIds().stream()
        .map(userId -> ReadStatusCreateRequest.from(channel.getId(), userId, Instant.MIN))
        .forEach(readStatusService::create);

    return channelMapper.toDto(savedChannel);
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelDto find(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(
            ErrorCode.CHANNEL_NOT_FOUND,
            Map.of("channelId", channelId)
        ));

    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> joinedChannels = readStatusRepository.findByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(BaseEntity::getId)
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel -> channel.getType() == Channel.ChannelType.PUBLIC || joinedChannels.contains(
            channel.getId()))
        .map(channelMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(
            ErrorCode.CHANNEL_NOT_FOUND,
            Map.of("channelId", channelId)
        ));

    if (channel.isPrivate()) {
      throw new PrivateChannelUpdateException(
          ErrorCode.PRIVATE_CHANNEL_UPDATE,
          Map.of("channelId", channel.getId())
      );
    }

    if (request.newName() != null) {
      channel.updateName(request.newName());
      log.info("Channel entity updated - name changed: id = {}", channel.getId());
    }
    if (request.newDescription() != null) {
      channel.updateDescription(request.newDescription());
      log.info("Channel entity updated - description changed: id = {}", channel.getId());
    }

    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public void delete(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new ChannelNotFoundException(
          ErrorCode.CHANNEL_NOT_FOUND,
          Map.of("channelId", channelId)
      );
    }
    channelRepository.deleteById(channelId);

    log.info("Channel entity deleted: id = {}", channelId);
  }
}
