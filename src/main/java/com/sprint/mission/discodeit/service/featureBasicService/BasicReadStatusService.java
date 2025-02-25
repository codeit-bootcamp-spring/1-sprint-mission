package com.sprint.mission.discodeit.service.featureBasicService;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatus create(ReadStatusCreate readStatusCreate) {
    UUID userId = readStatusCreate.userId();
    UUID channelId = readStatusCreate.channelId();

    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException(userId + "를 찾을 수 없습니다.");
    }
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException(channelId + "를 찾을 수 없습니다.");
    }
    if (readStatusRepository.findAllByUserId(userId).stream()
        .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId))) {
      throw new IllegalArgumentException(userId + "와" + channelId + "가 이미 존재합니다.");
    }

    Instant lastReadAt = readStatusCreate.lastReadAt();
    ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadAt);
    return readStatusRepository.save(readStatus);
  }

  @Override
  public ReadStatus findById(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException(readStatusId + "를 찾을 수 없습니다."));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .collect(Collectors.toList());
  }

  @Override
  public ReadStatus update(UUID readStatusId, ReadStatusUpdate readStatusUpdate) {
    Instant lastReadAt = readStatusUpdate.lastActiveAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException(readStatusId + "를 찾을 수 없습니다."));
    readStatus.update(lastReadAt);
    return readStatusRepository.save(readStatus);
  }

  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new NoSuchElementException(readStatusId + "를 찾을 수 없습니다.");
    }
    readStatusRepository.deleteById(readStatusId);
  }
}
