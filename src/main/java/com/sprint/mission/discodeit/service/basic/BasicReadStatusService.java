package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
      throws CustomException {

    if (createReadStatusDto == null || createReadStatusDto.channelId() == null
        || createReadStatusDto.userId() == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA);
    }
    Channel channel = channelRepository.findById(UUID.fromString(createReadStatusDto.channelId()))
        .orElse(null);
    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    User user = userRepository.findById(UUID.fromString(createReadStatusDto.userId())).orElse(null);
    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    ReadStatus readStatus = readStatusRepository.findByChannelIdAndUserId(
        UUID.fromString(createReadStatusDto.channelId()),
        UUID.fromString(createReadStatusDto.userId())).orElse(null);

    if (readStatus != null) {
      throw new CustomException(ErrorCode.READ_STATUS_ALREADY_EXIST);
    }

    readStatus = new ReadStatus(channel, user, createReadStatusDto.lastReadAt());
    ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatusDto findById(String readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(UUID.fromString(readStatusId))
        .orElse(null);
    if (readStatus == null) {
      throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
    }
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional
  public ReadStatusDto update(String readStatusId,
      UpdateReadStatusDto updateReadStatusDto) {

    if (updateReadStatusDto == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA);
    }

    ReadStatus readStatus = readStatusRepository.findById(UUID.fromString(readStatusId))
        .orElse(null);
    if (readStatus == null) {
      throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
    }

    readStatus.setLastReadAt(updateReadStatusDto.newLastReadAt());
    readStatus.setUpdatedAt(updateReadStatusDto.newLastReadAt());

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional
  public List<ReadStatusDto> updateByUserId(String userId,
      UpdateReadStatusDto updateReadStatusDto) {

    List<ReadStatus> readStatuses = readStatusRepository.findByUserId(UUID.fromString(userId));

    if (readStatuses == null || readStatuses.isEmpty()) {
      throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
    }

    List<ReadStatusDto> readStatusDtos = new ArrayList<>();

    //기능 미사용으로 임시 주석처리
//    for (ReadStatus readStatus : readStatuses) {
//      if (readStatus.isUpdated(updateReadStatusDto)) {
//        readStatusResponseDtos.add(ReadStatusResponseDto.from(readStatusRepository.save(readStatus),
//            isNewMessage(readStatus)));
//      } else {
//        readStatusResponseDtos.add(
//            ReadStatusResponseDto.from(readStatus, isNewMessage(readStatus)));
//      }
//    }
    return readStatusDtos;
  }

  @Override
  @Transactional
  public List<ReadStatusDto> updateByChannelId(String channelId,
      UpdateReadStatusDto updateReadStatusDto) {

    Channel channel = channelRepository.findById(UUID.fromString(channelId)).orElse(null);
    if (channel == null) {
      throw new CustomException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(
        UUID.fromString(channelId));
    if (readStatuses == null || readStatuses.isEmpty()) {
      throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
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
      throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
    }

    List<ReadStatusDto> readStatusDtos = new ArrayList<>();

    for (ReadStatus readStatus : allReadStatusByUserId) {
      readStatusDtos.add(readStatusMapper.toDto(readStatus));
    }
    return readStatusDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusDto> findAllByChannelId(String channelId) {
    return readStatusRepository.findByChannelId(UUID.fromString(channelId)).stream()
        .map(readStatusMapper::toDto).toList();
  }

  @Override
  @Transactional
  public boolean delete(String readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(UUID.fromString(readStatusId))
        .orElse(null);
    if (readStatus == null) {
      throw new CustomException(ErrorCode.READ_STATUS_NOT_FOUND);
    }
    readStatusRepository.delete(readStatus);
    return true;
  }
}
