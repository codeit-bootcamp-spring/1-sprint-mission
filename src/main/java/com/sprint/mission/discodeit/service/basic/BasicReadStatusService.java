package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ReadStatus.ReadStatusDuplicateException;
import com.sprint.mission.discodeit.exception.ReadStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.jpa.ChannelRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @Transactional
  public ReadStatusDto create(ReadStatusCreateDTO dto) {
    User findUser = userRepository.findById(dto.getUserId())
        .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));
    Channel findChannel = channelRepository.findById(dto.getChannelId())
        .orElseThrow(() -> new ChannelNotFoundException(dto.getChannelId()));
    if (readStatusRepository.existsByUserIdAndChannelId(findUser.getId(), findChannel.getId())) {
      throw new ReadStatusDuplicateException(Map.of(
          "userId", findUser.getId().toString(),
          "channelId", findChannel.getId().toString()
      ));
    }

    boolean notificationEnabled = switch (findChannel.getChannelType()) { //notificationEnabled 필드 추가
      case PRIVATE -> true;
      case PUBLIC -> false;
    };

    ReadStatus readStatus = readStatusRepository.save(
        new ReadStatus(findUser, findChannel, dto.getLastReadAt(), notificationEnabled)); //notificationEnabled 필드 추가
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public ReadStatusDto find(UUID id) {
    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new ReadStatusNotFoundException(id));
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public List<ReadStatusDto> findAll() {
    return readStatusRepository.findAll().stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUser_Id(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public ReadStatusDto update(UUID id, ReadStatusUpdateRequest dto) {
    ReadStatus findReadStatus = readStatusRepository.findById(id).
        orElseThrow(() -> new ReadStatusNotFoundException(id));
    findReadStatus.updateLastReadAt(dto.getNewLastReadAt());
    findReadStatus.updateNotificationEnabled(dto.isNewNotificationEnabled());
    return readStatusMapper.toDto(findReadStatus);
  }

  @Override
  public void delete(UUID id) {
    readStatusRepository.deleteById(id);
  }

}
