package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.repository.jpa.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validator.UserStatusValidator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusValidator userStatusValidator;

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  public UserStatusDto create(UserStatusCreateDTO dto) {
    userStatusValidator.validateUserStatus(dto.getUserid());

    User findUser = userRepository.findById(dto.getUserid())
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    UserStatus userStatus = new UserStatus(findUser, Instant.now());
    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto find(UUID id) {
    UserStatus userStatus = userStatusRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_STATUS_NOT_FOUND));
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateDTO userStatusUpdateDTO) {
    UserStatus findUserStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_STATUS_NOT_FOUND));
    findUserStatus.updateLastActiveAt(userStatusUpdateDTO.getNewLastActiveAt());
    return userStatusMapper.toDto(findUserStatus);
  }

  @Override
  @Transactional
  public UserStatusDto updateByUserId(UUID userId, Instant time) {
    UserStatus findUserStatus = userStatusRepository.findByUser_Id(userId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_STATUS_NOT_FOUND));
    findUserStatus.updateLastActiveAt(time);

    return userStatusMapper.toDto(findUserStatus);
  }

  @Override
  public void delete(UUID id) {
    userStatusRepository.deleteById(id);
  }

}
