package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.spirnt.mission.discodeit.enity.ReadStatus;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.ReadStatusRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusServiceImpl implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;


  @Override
  public ReadStatus create(ReadStatusCreateRequest readStatusCreateRequest) {
    UUID userId = readStatusCreateRequest.userId();
    UUID channelId = readStatusCreateRequest.channelId();
    // User와 Channel 존재하지 않으면 예외 발생
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("User with id " + userId + " not found");
    }
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel with id " + channelId + " not found");
    }
    //이미 해당 채널-유저를 가진 ReadStatus가 있으면 예외 발생
    if (existsByUserIdChannelId(userId,
        channelId)) {
      throw new IllegalArgumentException(
          "ReadStatus with userId " + userId + " and channelId "
              + channelId + " already exists");
    }
    ReadStatus readStatus = new ReadStatus(userId,
        readStatusCreateRequest.channelId(),
        readStatusCreateRequest.lastReadAt());  // API 스펙을 보고 lastReadAt을 클라이언트에서 받긴 했는데, 그냥 서버에서 Instant.now() 하는 게 더 안전하지 않을까?
    readStatusRepository.save(readStatus);
    return readStatus;
  }

  @Override
  public ReadStatus find(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("Read Status with id " + readStatusId + " not found"));
  }

  @Override
  public ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId) {
    return readStatusRepository.findByUserIdAndChannelId(userId, channelId)
        .orElseThrow(() -> new NoSuchElementException("Invalid User ID And Channel ID."));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    Map<UUID, ReadStatus> map = readStatusRepository.findAll();
    List<ReadStatus> list = map.values().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId))
        .collect(Collectors.toList());
    return list;
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    Map<UUID, ReadStatus> map = readStatusRepository.findAll();
    List<ReadStatus> list = map.values().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
        .collect(Collectors.toList());
    return list;
  }

  @Override
  public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("Read Status with id " + readStatusId + " not found"));
    readStatus.update(readStatusUpdateRequest.newLastReadAt());
    readStatusRepository.save(readStatus);
    return readStatus;
  }

  @Override
  public void delete(UUID id) {
    readStatusRepository.delete(id);
  }

  // 채널 서비스에서 호출
  @Override
  public void deleteByChannelId(UUID channelId) {
    Map<UUID, ReadStatus> map = readStatusRepository.findAll();
    List<UUID> list = map.values().stream()
        .filter(readStatus -> readStatus.getUserId().equals(channelId))
        .map(readStatus -> readStatus.getId())
        .collect(Collectors.toList());
    for (UUID id : list) {
      delete(id);
    }
  }

  @Override
  public boolean existsByUserIdChannelId(UUID userId, UUID channelId) {
    //이미 해당 채널-유저를 가진 ReadStatus가 있으면 예외 발생
    if (readStatusRepository.findAll().values().stream()
        .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId)
            && readStatus.getUserId().equals(userId))) {
      return true;
    }
    return false;
  }


}


