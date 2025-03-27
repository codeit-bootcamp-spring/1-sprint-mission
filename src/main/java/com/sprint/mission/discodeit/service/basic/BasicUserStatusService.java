package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  private final UserStatusMapper userStatusMapper;

  @Override
  public UserStatusDto create(UserStatusCreateDTO userStatusCreateDTO) {
    User user = userRepository.findById(userStatusCreateDTO.userId()).orElseThrow(
        () -> new NoSuchElementException("User not found"));

    //해당 User에 대한 UserStatus 객체 존재 검증
    if (userStatusRepository.existsByUserId(userStatusCreateDTO.userId())) {
      throw new NoSuchElementException("해당 사용자에 대한 UserStatus가 이미 존재합니다.");
    }

    UserStatus userStatus = UserStatus.builder()
        .user(user)
        .build();

    return userStatusMapper.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public void saveExist(UserStatus userStatus) {
    userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatusDto find(UUID uuid) {
    UserStatus userStatus = userStatusRepository.findById(uuid).orElseThrow(
        () -> new NoSuchElementException("userStatus not found"));
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll()
        .stream().map(u -> userStatusMapper.toDto(u)).collect(Collectors.toList());
  }

  private UserStatus findByUserId(UUID userId) {
    return userStatusRepository.findByUserId(userId);
  }


  @Override
  public UserStatusDto update(UUID id, UserStatusUpdateDTO userStatusUpdateDTO) {
    UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(
        () -> new NoSuchElementException("userStatus not found"));
    userStatus.update(userStatusUpdateDTO);
    return userStatusMapper.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatus updateByUserId(UUID userID, UserStatusUpdateDTO userStatusUpdateDTO) {
    UserStatus userStatus = userStatusRepository.findByUserId(userID);
    userStatus.update(userStatusUpdateDTO);
    userStatusRepository.save(userStatus);
    return userStatus;
  }

  @Override
  public void delete(UUID uuid) {
    userStatusRepository.deleteById(uuid);
  }
}
