package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReadStatusServiceImpl implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatus create(CreateReadStatusDto dto, boolean skipValidation) {

    if (!skipValidation) {
      validateUserExists(dto.userId());
      validateChannelExists(dto.channelId());
    }

    validateDuplicateUserChannelStatus(dto.userId(), dto.channelId());

    return readStatusRepository.save(new ReadStatus(dto.channelId(), dto.userId()));
  }

  private void validateChannelExists(String channelId) {
    if (channelRepository.findById(channelId).isEmpty()) {
      throw new ChannelNotFoundException();
    }
  }

  private void validateUserExists(String userId) {
    if (userRepository.findById(userId).isEmpty()) {
      throw new UserNotFoundException();
    }
  }

  private void validateDuplicateUserChannelStatus(String userId, String channelId) {
    if (readStatusRepository.findByUserId(userId).stream()
        .anyMatch(status -> status.getChannelId().equals(channelId))) {
      log.info("이미 존재하는 user + channel status 입니다={} + {}", userId, channelId);
      throw new InvalidOperationException(DEFAULT_ERROR_MESSAGE);
    }
  }

  @Override
  public ReadStatus find(String id) {
    return readStatusRepository.findById(id).orElseThrow( () -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE));
  }

  @Override
  public List<ReadStatus> findAllByUserId(String userId) {
    return readStatusRepository.findByUserId(userId);
  }

  @Override
  public List<ReadStatus> findAllByChannelId(String channelId) {
    return readStatusRepository.findByChannelId(channelId);
  }

  @Override
  public ReadStatus update(CreateReadStatusDto dto) {
    ReadStatus readStatus = readStatusRepository.findByChannelIdAndUserId(dto.channelId(), dto.userId()).orElseThrow(() -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE));
    readStatus.updateLastReadAt();
    readStatusRepository.save(readStatus);
    return readStatus;
  }

  @Override
  public void deleteByChannel(String id) {
    readStatusRepository.deleteByChannelId(id);
  }

  @Override
  public void delete(String id) {
    readStatusRepository.deleteByChannelId(id);
  }
}
