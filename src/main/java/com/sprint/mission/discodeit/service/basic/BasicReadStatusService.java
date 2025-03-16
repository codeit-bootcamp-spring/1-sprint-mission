package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.read_status.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.read_status.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
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

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatus create(ReadStatusCreateRequest request) {

    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));

    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new NoSuchElementException("채널이 존재하지 않습니다."));

    if (readStatusRepository.findAllByUserId(user.getId()).stream()
        .anyMatch(readStatus -> readStatus.getChannel().getId().equals(channel.getId()))) {
      throw new IllegalArgumentException("수신정보가 이미 존재합니다.");
    }

    ReadStatus readStatus = new ReadStatus(channel, user);
    return readStatusRepository.save(readStatus);
  }

  @Override
  public ReadStatus find(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException("수신정보가 존재하지 않습니다."));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .toList();
  }

  @Override
  public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException("수신정보가 존재하지 않습니다."));
    readStatus.update(newLastReadAt);
    return readStatusRepository.save(readStatus);
  }

  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsId(readStatusId)) {
      throw new NoSuchElementException("수신정보가 존재하지 않습니다.");
    }
    readStatusRepository.deleteById(readStatusId);
  }
}
