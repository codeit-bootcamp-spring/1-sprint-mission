package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusDuplicateException;
import com.sprint.mission.discodeit.exception.userStatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.repository.jpa.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {


  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  public UserStatusDto create(UserStatusCreateDTO dto) {

    User findUser = userRepository.findById(dto.getUserid())
        .orElseThrow(() -> new UserNotFoundException(dto.getUserid()));

    if (userStatusRepository.findByUser_Id(findUser.getId()).isPresent()) {
      throw new UserStatusDuplicateException("userId", findUser.getId().toString());
    }

    UserStatus userStatus = new UserStatus(findUser, Instant.now());
    userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto find(UUID id) {
    UserStatus userStatus = userStatusRepository.findById(id)
        .orElseThrow(() -> new UserStatusNotFoundException(id));
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
        .orElseThrow(() -> new UserStatusNotFoundException(userStatusId));
    findUserStatus.updateLastActiveAt(userStatusUpdateDTO.getNewLastActiveAt());
    return userStatusMapper.toDto(findUserStatus);
  }

  @Override
  @Transactional
  public UserStatusDto updateByUserId(UUID userId, Instant time) {
    UserStatus findUserStatus = userStatusRepository.findByUser_Id(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    findUserStatus.updateLastActiveAt(time);

    return userStatusMapper.toDto(findUserStatus);
  }

  @Override
  public void delete(UUID id) {
    userStatusRepository.deleteById(id);
  }

}
