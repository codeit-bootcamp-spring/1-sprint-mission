package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.ReadStatusValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final ReadStatusValidator readStatusValidator;
  private final UserValidator userValidator;
  private final ChannelValidator channelValidator;
  private final ReadStatusMapper readStatusMapper;

  @Override
  public ReadStatusDto createReadStatus(CreateReadStatusRequest createReadStatusRequest) {
    User user = userValidator.validateUserExistsByUserId(createReadStatusRequest.userId());
    Channel channel = channelValidator.validateChannelExistsByChannelId(
        createReadStatusRequest.channelId());

    return readStatusMapper.toReadStatusDto(
        readStatusRepository.saveReadStatus(
            ReadStatus.of(user, channel, createReadStatusRequest.lastReadAt())));
  }

  @Override
  public ReadStatus findReadStatusById(UUID readStatusId) {
    return readStatusValidator.validateReadStatusExistsById(readStatusId);
  }

  @Override
  public List<ReadStatusDto> findAllReadStatusesByUserId(UUID userId) {
    userValidator.validateUserExistsByUserId(userId);
    return readStatusRepository.findAllReadStatusByUserId(userId).stream()
        .map(readStatus -> readStatusMapper.toReadStatusDto(readStatus))
        .toList();
  }

  @Override
  public ReadStatusDto updateReadStatus(UUID readStatusId,
      UpdateReadStatusRequest updateReadStatusRequest) {
    ReadStatus readStatus = readStatusValidator.validateReadStatusExistsById(readStatusId);
    readStatus.updateLastReadAt(updateReadStatusRequest.newLastReadAt());

    return readStatusMapper.toReadStatusDto(readStatus);
  }

  @Override
  public void deleteReadStatus(UUID readStatusId) {
    readStatusValidator.validateReadStatusExistsById(readStatusId);

    readStatusRepository.removeReadStatus(readStatusId);
  }
}
