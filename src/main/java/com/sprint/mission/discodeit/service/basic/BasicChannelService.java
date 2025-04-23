package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(PublicChannelCreateRequest publicChannelCreateRequest) {
    log.debug("public 채널 생성 시작: {}", publicChannelCreateRequest);
    Channel channel = new Channel(ChannelType.PUBLIC, publicChannelCreateRequest.name(),
        publicChannelCreateRequest.description());
    channelRepository.save(channel);
    log.info("public 채널 생성 완료: id={}, name={}", channel.getId(), channel.getName());
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest privateChannelCreateRequest) {
    log.debug("private 채널 생성 시작: {}", privateChannelCreateRequest);
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);

    privateChannelCreateRequest.participantIds().forEach(userId -> {
      User participant = userRepository.findById(userId)
          .orElseThrow(() -> UserNotFoundException.withId(userId));
      ReadStatus readStatus = new ReadStatus(createdChannel, participant);
      readStatusRepository.save(readStatus);
    });
    log.info("private 채널 생성 완료: id={}, name={}", channel.getId(), channel.getName());
    return channelMapper.toDto(createdChannel);
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto find(UUID channelId) {
    log.debug("채널 조회 시작: id={}", channelId);
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.withId(channelId));
    log.info("채널 조회 완료: id={}", channelId);
    return channelMapper.toDto(channel);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findPublicAll() {
    log.debug("모든 public 채널 조회 시작");
    List<ChannelDto> channelList = channelRepository.findAll().stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC)
        .map(channelMapper::toDto)
        .toList();
    log.info("모든 public 채널 조회 완료: 총 {}개", channelList.size());
    return channelList;
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC)
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId,
      PublicChannelUpdateRequest publicChannelUpdateRequest) {
    log.debug("채널 수정 시작: id={}, request={}", channelId, publicChannelUpdateRequest);
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.withId(channelId));

    channel.update(publicChannelUpdateRequest.newName(),
        publicChannelUpdateRequest.newDescription());
    channelRepository.save(channel);
    log.info("채널 수정 완료: id={}, name={}", channelId, channel.getName());
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId, UUID adminId) {
    log.debug("채널 삭제 시작: id={}", channelId);
    if (!channelRepository.existsById(channelId)) {
      throw ChannelNotFoundException.withId(channelId);
    }
    log.info("채널 삭제 완료: id={}", channelId);
    channelRepository.deleteById(channelId);
  }

  @Transactional(readOnly = true)
  @Override
  public List<UUID> getParticipantIds(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.withId(channelId));
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      return readStatusRepository.findAllByChannelId(channelId)
          .stream()
          .map(readStatus -> readStatus.getChannel().getId())
          .toList();
    }
    return List.of();
  }

}
