package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  //
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {

    log.debug("create public채널 받는 값 : request={}", request);

    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);

    channelRepository.save(channel);

    log.info("create 채널 생성 성공 : name={}, description={}", channel.getName(), channel.getDescription());

    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {

    log.debug("create private채널 받는 값 : request={}", request);

    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);

    List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
        .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
        .toList();
    readStatusRepository.saveAll(readStatuses);

    log.info("create 성공: 채널 ID={}, 참여자 IDs={}", channel.getId(),
        readStatuses.stream()
            .map(status -> status.getUser().getId().toString())
            .collect(Collectors.joining(", ")));


    return channelMapper.toDto(channel);
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(
            () -> {
              log.error("find 실패 채널을 찾을 수 없음 : channelId={}", channelId);
              return new ChannelNotFoundException(Map.of("channelId", channelId));
            });
  }

  @Transactional(readOnly = true)
  @Override
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
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    log.debug("update 받는 값 : request={}", request);
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.error("update 실패: channelId={}인 채널을 찾을 수 없음", channelId);
              return new ChannelNotFoundException(Map.of("channelId", channelId));
            });
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      log.warn("update 실패: channelId={}는 비공개 채널이므로 업데이트 불가", channelId);
      throw new PrivateChannelUpdateNotAllowedException(Map.of("ChannelType",channel.getType()));
    }
    channel.update(newName, newDescription);

    log.info("update 성공: channelId={}", channelId);

    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      log.error("delete 실패: channelId={}인 채널을 찾을 수 없음", channelId);
      throw new ChannelNotFoundException(Map.of("channelId", channelId));
    }

    log.debug("delete 메시지 삭제 시작: 채널 ID={}", channelId);
    messageRepository.deleteAllByChannelId(channelId);
    log.debug("delete 메시지 삭제 완료: 채널 ID={}", channelId);

    log.debug("delete 읽기 상태 삭제 시작: 채널 ID={}", channelId);
    readStatusRepository.deleteAllByChannelId(channelId);
    log.debug("delete 읽기 상태 삭제 완료: 채널 ID={}", channelId);

    channelRepository.deleteById(channelId);
    log.info("delete 완료: ID={}", channelId);
  }
}
