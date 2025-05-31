package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusAlreadyExistException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @PreAuthorize("principal.user.id == #dto.userId()")
  @Transactional
  public ReadStatusDto create(ReadStatusCreateRequest dto) {
    UUID userId = dto.userId();
    UUID channelId = dto.channelId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Map.of("id", userId)));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(Map.of("id", channelId)));

    if (readStatusRepository.existsByUser_IdAndChannel_Id(userId, channelId)) {
      throw new ReadStatusAlreadyExistException(Map.of());
    }
    ReadStatus readStatus = readStatusRepository.save(ReadStatus.create(user, channel));
    return readStatusMapper.toDto(readStatus);
  }

  private ReadStatus findById(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new ReadStatusNotFoundException(Map.of("id", id)));
  }

  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      return Collections.emptyList();
    } else {
      return readStatusRepository.findByUserId(userId).stream()
          .map(readStatusMapper::toDto)
          .toList();
    }
  }

  @PostAuthorize("principal.user.id == returnObject.userId()")
  @Transactional
  public ReadStatusDto update(UUID id, Instant newLastReadAt) {
    ReadStatus readStatus = findById(id);
    readStatus.updateLastReadAt(newLastReadAt);
    readStatusRepository.save(readStatus);
    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  public void delete(UUID id) {
    readStatusRepository.deleteById(id);
  }
}