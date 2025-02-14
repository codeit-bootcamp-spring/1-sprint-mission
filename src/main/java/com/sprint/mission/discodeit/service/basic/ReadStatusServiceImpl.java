package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReadStatusServiceImpl implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final EntityValidator validator;

  @Override
  public ReadStatus create(CreateReadStatusDto dto, boolean skipValidation) {

    if (!skipValidation) {
      validator.findOrThrow(User.class, dto.userId(), new UserNotFoundException());
      validator.findOrThrow(Channel.class, dto.channelId(), new ChannelNotFoundException());
    }

    validateDuplicateUserChannelStatus(dto.userId(), dto.channelId());

    return readStatusRepository.save(new ReadStatus(dto.channelId(), dto.userId()));
  }

  // TODO : repository 에 saveAll(), 필요시 validation 추가
  @Override
  public List<ReadStatus> createMultipleReadStatus(List<String> userIds, String channelId) {
    return userIds.stream()
        .map(userId -> {
          ReadStatus status = new ReadStatus(channelId, userId);
          readStatusRepository.save(status);
          return status;
        }).toList();
  }

  private void validateDuplicateUserChannelStatus(String userId, String channelId) {
    boolean exists = readStatusRepository.findByUserId(userId).stream()
        .anyMatch(status -> status.getChannelId().equals(channelId));

    if (exists) {
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
  public Map<String, List<String>> getUserIdsForChannelReadStatuses(List<Channel> channels) {
    List<String> channelIds = channels.stream().map(Channel::getUUID).toList();
    return channelIds.stream()
        .collect(Collectors.toMap(
            id -> id,
            id -> readStatusRepository.findByChannelId(id).stream()
                .map(ReadStatus::getUserId).toList()
        ));
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
