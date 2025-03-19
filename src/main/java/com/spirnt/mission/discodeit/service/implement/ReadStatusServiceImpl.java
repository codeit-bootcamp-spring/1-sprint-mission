package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ReadStatus;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.mapper.ReadStatusMapper;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.ReadStatusRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusServiceImpl implements ReadStatusService {

  private final ReadStatusMapper readStatusMapper;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;


  @Override
  public ReadStatusDto create(ReadStatusCreateRequest readStatusCreateRequest) {
    UUID userId = readStatusCreateRequest.userId();
    UUID channelId = readStatusCreateRequest.channelId();
    Instant lastReadAt = readStatusCreateRequest.lastReadAt();
    // User와 Channel 존재하지 않으면 예외 발생
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    //이미 해당 채널-유저를 가진 ReadStatus가 있으면 예외 발생
    if (readStatusRepository.existsByUserIdAndChannelId(userId,
        channelId)) {
      throw new IllegalArgumentException(
          "ReadStatus with userId " + userId + " and channelId "
              + channelId + " already exists");
    }
    ReadStatus readStatus = readStatusRepository.save(new ReadStatus(user, channel, lastReadAt));
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public ReadStatusDto find(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("Read Status with id " + readStatusId + " not found"));
    return readStatusMapper.toDto(readStatus);
  }


  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
    readStatusRepository.updateById(readStatusId, readStatusUpdateRequest.newLastReadAt());
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("Read Status with id " + readStatusId + " not found"));
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public void delete(UUID id) {
    readStatusRepository.deleteById(id);
  }
}


