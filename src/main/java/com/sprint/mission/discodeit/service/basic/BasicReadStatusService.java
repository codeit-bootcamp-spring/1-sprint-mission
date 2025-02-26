package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public void create(ReadStatusDto readStatusDto) {
    userRepository.findById(readStatusDto.userId())
        .orElseThrow(
            () -> new NoSuchElementException("user not found with id: " + readStatusDto.userId()));
    channelRepository.findById(readStatusDto.channelId())
        .orElseThrow(() -> new NoSuchElementException(
            "channel not found with id: " + readStatusDto.channelId()));

    readStatusRepository.findAllByUser(readStatusDto.userId()).forEach(r -> {
      if (r.getChannelId().equals(readStatusDto.channelId())) {
        throw new IllegalArgumentException(
            "read status already exists with channel id: " + readStatusDto.channelId());
      }
    });

    ReadStatus readStatus = new ReadStatus(readStatusDto.userId(), readStatusDto.channelId());
    readStatusRepository.save(readStatus);
  }

  @Override
  public ReadStatus findById(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new NoSuchElementException("read status not found with id: " + readStatusId));
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    List<ReadStatus> readStatus = readStatusRepository.findAllByUser(userId);
    return readStatus.stream()
        .map(r -> new ReadStatusDto(r.getUserid(), r.getChannelId(), r.getLastReadAt()))
        .toList();
  }

  @Override
  public void updateReadStatus(UUID userId, UUID channelId) {
    Channel channel = channelRepository
        .findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("channel not found with id: " + channelId));
    ReadStatus readStatus = readStatusRepository.findAllByUser(userId).stream()
        .filter(r -> r.getChannelId().equals(channelId)).findFirst()
        .orElseThrow(() -> new NoSuchElementException(
            "read status not found with channel id: " + channelId));
    readStatus.updateLastReadAt();
  }

  @Override
  public void remove(UUID readStatusId) {
    readStatusRepository.remove(readStatusId);
  }
}
