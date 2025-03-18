package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ChannelValidator validator;
  private final ChannelMapper channelMapper;

  private final ReadStatusService readStatusService;
  private final ReadStatusRepository readStatusRepository;

  @Override
  @Transactional
  public ChannelDto create(PublicChannelCreateRequest request) {
    return channelMapper.toDto(
        channelRepository.save(
            new Channel(Channel.ChannelType.PUBLIC, request.name(), request.description()))
    );
  }

  @Override
  @Transactional
  public ChannelDto create(PrivateChannelCreateRequest request) {
    Channel channel = channelRepository.save(new Channel(Channel.ChannelType.PRIVATE, null, null));

    request.participantsIds().stream()
        .map(userId -> ReadStatusCreateRequest.from(channel.getId(), userId, Instant.MIN))
        .forEach(readStatusService::create);

    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelDto find(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));

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
    validator.validate(request.newName(), request.newDescription());
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다."));
    channel.update(request.newName(), request.newDescription());

    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional
  public void delete(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("[ERROR] 존재하지 않는 채널입니다.");
    }
    channelRepository.deleteById(channelId);
  }
}
