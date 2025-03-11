package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final UserService userService;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelService channelService;

  @Override
  public ReadStatus create(CreateReadStatusRequest request) {
    User user = userService.getUserById(request.userId());
    Channel channel = channelService.getChannel(request.channelId());

    Instant lastReadTime = request.lastReadTime();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadTime);

    return readStatusRepository.save(readStatus);
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    User user = userService.getUserById(userId);
    return readStatusRepository.findAllByOwner(user);
  }

  @Override
  public ReadStatus findById(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId).orElseThrow(
        () -> new NoSuchElementException("Read status not found")
    );
  }

  @Override
  public List<ReadStatus> findAll() {
    return (List<ReadStatus>) readStatusRepository.findAll();
  }

  @Override
  public synchronized void updateReadStatusByUserIdAndChannelId(UUID userId, UUID channelId) {
    User user = userService.getUserById(userId);
    Channel channel = channelService.getChannel(channelId);
    readStatusRepository.findAllByOwnerAndChannel(user, channel).stream()
        .peek(
            readStatus -> readStatus.updateLastReadTime(Instant.now())
        );
  }

  @Override
  public synchronized ReadStatus updateReadStatusById(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElseThrow(
        () -> new NoSuchElementException("Read status not found")
    );
    readStatus.updateLastReadTime(Instant.now());
    return readStatusRepository.save(readStatus);
  }


}
