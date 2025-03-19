package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.jpa.ChannelRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.ReadStatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusValidator readStatusValidator;

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @Transactional
  public ReadStatusDto create(ReadStatusCreateDTO dto) {
    User findUser = userRepository.findById(dto.getUserId())
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    Channel findChannel = channelRepository.findById(dto.getChannelId())
        .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    ReadStatus readStatus = readStatusRepository.save(
        new ReadStatus(findUser, findChannel, dto.getLastReadAt()));
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public ReadStatusDto find(UUID id) {
    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.READ_STATUS_NOT_FOUND));
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public List<ReadStatusDto> findAll() {
    return readStatusRepository.findAll().stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUser_Id(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public ReadStatusDto update(UUID id, ReadStatusUpdateDTO dto) {
    ReadStatus findReadStatus = readStatusRepository.findById(id).
        orElseThrow(() -> new NotFoundException(ErrorCode.READ_STATUS_NOT_FOUND));
    findReadStatus.updateLastReadAt(dto.getNewLastReadAt());
    return readStatusMapper.toDto(findReadStatus);
  }

  @Override
  public void delete(UUID id) {
    readStatusRepository.deleteById(id);
  }

}
