package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

  @Override
  public ReadStatus create(CreateReadStatusRequestDto request) {
    if (!channelRepository.existsById(request.getChannelId())) {
      throw new NoSuchElementException("channel not found");
    }
    if (!userRepository.existsById(request.getUserId())) {
      throw new NoSuchElementException("user not found");
    }
    if (readStatusRepository.isUserMemberOfChannel(request.getChannelId(), request.getUserId())) {
      throw new NoSuchElementException("user already has member of channel");
    }

    Instant now = request.getLastReadAt();
    ReadStatus readStatus = new ReadStatus(request.getUserId(), request.getChannelId(), now);
    readStatusRepository.save(readStatus);
    return readStatus;
  }

  @Override
  public ReadStatus find(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("readStatus not found"));
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId);
  }

  @Override
  public ReadStatus update(UUID readStatusId, UpdateReadStatusRequestDto request) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException("ReadStatus id not found"));
    readStatus.update(request.getNewLastReadAt());
    return readStatusRepository.save(readStatus);
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return readStatusRepository.findAllByChannelId(channelId);
  }

  @Override
  public void delete(UUID id) {
    if (!readStatusRepository.existsById(id)) {
      throw new NoSuchElementException("readStatus not found");
    }
    readStatusRepository.deleteById(id);
  }

  @Override
  public void deleteByChannelId(UUID id) {
    readStatusRepository.deleteByChannelId(id);
  }
}
