package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @Transactional
  public ReadStatusDto create(CreateReadStatusDto createReadStatusDto)
      throws DiscodeitException {

    if (createReadStatusDto == null || createReadStatusDto.channelId() == null
        || createReadStatusDto.userId() == null) {
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }
    Channel channel = channelRepository.findById(UUID.fromString(createReadStatusDto.channelId()))
        .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

    User user = userRepository.findById(UUID.fromString(createReadStatusDto.userId()))
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    ReadStatus readStatus = readStatusRepository.findByChannelIdAndUserId(
        UUID.fromString(createReadStatusDto.channelId()),
        UUID.fromString(createReadStatusDto.userId())).orElse(null);

    if (readStatus != null) {
      throw new DiscodeitException(ErrorCode.READ_STATUS_ALREADY_EXIST);
    }

    if (channel.getType() == ChannelType.PRIVATE) {
      readStatus = new ReadStatus(channel, user, createReadStatusDto.lastReadAt(), true);
    } else {
      readStatus = new ReadStatus(channel, user, createReadStatusDto.lastReadAt(), false);
    }
    readStatusRepository.save(readStatus);

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatusDto findById(String readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(UUID.fromString(readStatusId))
        .orElse(null);
    if (readStatus == null) {
      throw new ReadStatusNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND);
    }
    return readStatusMapper.toDto(readStatus);
  }

  @PostAuthorize("authentication.principal.userDto.id == returnObject.userId()")
  @Override
  @Transactional
  public ReadStatusDto update(String readStatusId,
      UpdateReadStatusDto updateReadStatusDto) {

    if (updateReadStatusDto == null) {
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }

    ReadStatus readStatus = readStatusRepository.findById(UUID.fromString(readStatusId))
        .orElseThrow(() -> new ReadStatusNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND));

    readStatus.setLastReadAt(updateReadStatusDto.newLastReadAt());
    readStatus.setUpdatedAt(updateReadStatusDto.newLastReadAt());

    return readStatusMapper.toDto(readStatus);
  }


    List<ReadStatusDto> readStatusDtos = new ArrayList<>();
    for (ReadStatus readStatus : readStatuses) {
      readStatus.setUpdatedAt(updateReadStatusDto.newLastReadAt());
      readStatusDtos.add(readStatusMapper.toDto(readStatus));
    }
    return readStatusDtos;
  }

  public List<ReadStatusDto> findAllByUserId(String userId) {

    List<ReadStatus> allReadStatusByUserId = readStatusRepository.findByUserId(
        UUID.fromString(userId));

    if (allReadStatusByUserId == null) {
      throw new ReadStatusNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND);
    }

    List<ReadStatusDto> readStatusDtos = new ArrayList<>();

    for (ReadStatus readStatus : allReadStatusByUserId) {
      readStatusDtos.add(readStatusMapper.toDto(readStatus));
    }
    return readStatusDtos;
  }

  @Override
  @Transactional
  public boolean delete(String readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(UUID.fromString(readStatusId))
        .orElseThrow(() -> new ReadStatusNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND));

    readStatusRepository.delete(readStatus);
    return true;
  }
}
