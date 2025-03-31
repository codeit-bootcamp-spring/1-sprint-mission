package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChannelService {

  private final ChannelRepository channelRepository;
  //
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  public ChannelDto create(PublicChannelCreateRequest request) {
    log.debug("공개 채널 생성 서비스 진입 - name: {}, description: {}", request.name(), request.description());

    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);

    channelRepository.save(channel);
    log.info("공개 채널 생성 완료 - channelId: {}, name: {}", channel.getId(), request.name());

    return channelMapper.toDto(channel);
  }

  @Transactional
  public ChannelDto create(PrivateChannelCreateRequest request) {
    log.debug("비공개 채널 생성 서비스 진입 - participantIds: {}", request.participantIds());
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);

    List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
        .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
        .toList();
    readStatusRepository.saveAll(readStatuses);
    log.info("비공개 채널 생성 완료 - channelId: {}, participantCount: {}", channel.getId(),
        readStatuses.size());

    return channelMapper.toDto(channel);
  }

  @Transactional(readOnly = true)
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
  }

  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    log.debug("채널 업데이트 시작 - channelId: {}, newName: {}, newDescription: {}",
        channelId, request.newName(), request.newDescription());

    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseGet(
            () -> {
              log.warn("업데이트 실패 - 존재하지 않는 채널 - channelId: {}", channelId);
              throw new NoSuchElementException("Channel with id " + channelId + " not found");
            });
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      log.warn("업데이트 실패 - 비공개 채널 업데이트 시도 - channelId: {}", channelId);
      throw new IllegalArgumentException("Private channel cannot be updated");
    }
    channel.update(newName, newDescription);
    log.info("채널 업데이트 완료 - channelId: {}", channelId);

    return channelMapper.toDto(channel);
  }

  @Transactional
  public void delete(UUID channelId) {
    log.debug("채널 삭제 서비스 진입 - channelId: {}", channelId);
    if (!channelRepository.existsById(channelId)) {
      log.warn("채널 삭제 실패 - 존재하지 않는 채널 - channelId: {}", channelId);
      throw new NoSuchElementException("Channel with id " + channelId + " not found");
    }

    messageRepository.deleteAllByChannelId(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);
    channelRepository.deleteById(channelId);
    log.info("채널 삭제 완료 - channelId: {}", channelId);
  }
}
