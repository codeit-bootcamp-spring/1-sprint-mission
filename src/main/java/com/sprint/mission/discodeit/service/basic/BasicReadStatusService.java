package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final ReadStatusMapper readStatusMapper;

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  @Transactional
  @PreAuthorize("#request.userId == principal.user.id")
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new UserNotFoundException(
            ErrorCode.USER_NOT_FOUND,
            Map.of("userId", request.userId())
        ));
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new ChannelNotFoundException(
            ErrorCode.CHANNEL_NOT_FOUND,
            Map.of("channelId", request.channelId())
        ));

    readStatusRepository.findByUserId(request.userId())
        .forEach(readStatus -> {
          if (readStatus.isSameChannelById(request.channelId())) {
            throw new ReadStatusAlreadyExistsException(
                ErrorCode.READ_STATUS_ALREADY_EXISTS,
                Map.of(
                    "readStatusId", readStatus.getId(),
                    "userId", request.userId(),
                    "channelId", request.channelId()
                )
            );
          }
        });

    return readStatusMapper.toDto(
        readStatusRepository.save(new ReadStatus(user, channel, request.lastReadAt()))
    );
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatusDto find(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .map(readStatusMapper::toDto)
        .orElseThrow(() -> new ReadStatusNotFoundException(
            ErrorCode.READ_STATUS_NOT_FOUND,
            Map.of("readStatusId", readStatusId)
        ));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  @PreAuthorize("@authz.isReadStatusOwner(#readStatusId, principal.user.id)")
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ReadStatusNotFoundException(
            ErrorCode.READ_STATUS_NOT_FOUND,
            Map.of("readStatusId", readStatusId)
        ));
    if (request.newLastReadAt() != null) {
      readStatus.update(request.newLastReadAt());
    }

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new ReadStatusNotFoundException(
          ErrorCode.READ_STATUS_NOT_FOUND,
          Map.of("readStatusId", readStatusId)
      );
    }
    readStatusRepository.deleteById(readStatusId);
  }
}
