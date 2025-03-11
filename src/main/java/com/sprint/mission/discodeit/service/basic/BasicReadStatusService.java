package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatusResponse create(ReadStatusRequest.Create request) {
    User user = userRepository.findById(request.userId()).orElseThrow(
        () -> new RestApiException(ErrorCode.USER_NOT_FOUND, "userId : " + request.userId()));
    Channel channel = channelRepository.findById(request.channelId()).orElseThrow(
        () -> new RestApiException(ErrorCode.CHANNEL_NOT_FOUND,
            "channelId : " + request.channelId()));

    if (readStatusRepository.existsByUserIdAndChannelId(request.userId(), request.channelId())) {
      throw new RestApiException(ErrorCode.READ_IS_ALREADY_EXIST, "");
    }
    ReadStatus newReadStatus = ReadStatus.createReadStatus(user, channel, request.lastReadAt());

    readStatusRepository.save(newReadStatus);
    log.info("Create Read Status : {}", newReadStatus);
    return ReadStatusResponse.entityToDto(newReadStatus);
  }

  @Override
  public ReadStatusResponse findById(UUID id) {
    return ReadStatusResponse.entityToDto(findByIdOrThrow(id));
  }

  @Override
  public ReadStatus findByIdOrThrow(UUID id) {
    return readStatusRepository.findById(id)
        .orElseThrow(() -> new RestApiException(ErrorCode.READ_STATUS_NOT_FOUND, "id : " + id));
  }

  @Override
  public List<ReadStatusResponse> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatusResponse::entityToDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<ReadStatusResponse> findAllByChannelId(UUID channelId) {
    return readStatusRepository.findAllByChannelId(channelId).stream()
        .map(ReadStatusResponse::entityToDto)
        .collect(Collectors.toList());
  }

  @Override
  public ReadStatusResponse update(UUID id, ReadStatusRequest.Update request) {
    ReadStatus readStatus = findByIdOrThrow(id);
    readStatus.updateLastReadAt(request.newLastReadAt());
    return ReadStatusResponse.entityToDto(readStatusRepository.save(readStatus));
  }

  @Override
  public void deleteById(UUID id) {
    readStatusRepository.deleteById(id);
  }

  @Override
  public void deleteAllByChannelId(UUID channelId) {
    readStatusRepository.deleteAllByChannelId(channelId);
  }
}
