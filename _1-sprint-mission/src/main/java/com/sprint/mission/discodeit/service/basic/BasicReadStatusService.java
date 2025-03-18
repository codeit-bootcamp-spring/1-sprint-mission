package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.Locked.Read;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  public ReadStatusDto create(ReadStatusCreateRequest readStatusCreateRequest) {
    //유저와 채널 존재 여부 확인
    if (!userRepository.existsById(readStatusCreateRequest.userId())) {
      throw new NoSuchElementException(
          "User with id " + readStatusCreateRequest.userId() + " does not exist");
    }
    if (channelRepository.existsById(readStatusCreateRequest.channelId())) {
      throw new NoSuchElementException(
          "Channel with id " + readStatusCreateRequest.channelId() + " does not exist");
    }
    //이미 존재하는 경우 예외 발생
    if (readStatusRepository.findAllByUserId(readStatusCreateRequest.userId()).stream()
        .anyMatch(
            readStatus -> readStatus.getChannel().getId()
                .equals(readStatusCreateRequest.channelId()))) {
      throw new IllegalArgumentException(
          "ReadStatus with userId " + readStatusCreateRequest.userId() + " and channelId "
              + readStatusCreateRequest.channelId() + " already exists");
    }
    User user = userRepository.findById(readStatusCreateRequest.userId()).orElseThrow(
        () -> new NoSuchElementException(
            "User with id " + readStatusCreateRequest.userId() + " not found"));
    Channel channel = channelRepository.findById(readStatusCreateRequest.channelId()).orElseThrow(
        () -> new NoSuchElementException(
            "Channel with id " + readStatusCreateRequest.channelId() + " not found"));
    ReadStatus readStatus = new ReadStatus(user, channel, readStatusCreateRequest.lastReadAt());
    ReadStatus savedReadStatus = readStatusRepository.save(readStatus);
    return readStatusMapper.toDto(savedReadStatus);
  }

  @Override
  public ReadStatusDto findById(UUID readStatusId) {
    ReadStatus find = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
    return readStatusMapper.toDto(find);
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId).stream().toList();
    return readStatuses.stream().map(readStatusMapper::toDto).toList();
  }

  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
    readStatus.updateLastReadAt(readStatusUpdateRequest.newLastReadAt());
    ReadStatus savedReadStatus = readStatusRepository.save(readStatus);
    return readStatusMapper.toDto(savedReadStatus);
  }

  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new NoSuchElementException("ReadStatus with id " + readStatusId + " not found");
    }
    readStatusRepository.deleteById(readStatusId);
  }


}
