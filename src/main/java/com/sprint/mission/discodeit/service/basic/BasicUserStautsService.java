package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStautsService implements UserStatusService {
  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  
  @Override
  public void create(UserStatusDto userStatusDto) {
    userRepository.findById(userStatusDto.userId())
        .orElseThrow(() -> new NoSuchElementException("user not found with id: " + userStatusDto.userId()));
    
    if (userStatusRepository.findByUserId(userStatusDto.userId()).isEmpty()) {
      throw new IllegalArgumentException("user status already exists with user id: " + userStatusDto.userId());
    }
    
    UserStatus userStatus = new UserStatus(userStatusDto.userId());
    userStatusRepository.save(userStatus);
  }
  
  @Override
  public UserStatus findById(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("user status not found with id: " + userStatusId));
  }
  
  @Override
  public boolean isOnline(UUID userId) {
    return userStatusRepository.isOnline(userId);
  }
  
  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }
  
  @Override
  public void update(UUID userStatusId) {
    userStatusRepository.findById(userStatusId)
        .ifPresentOrElse(UserStatus::updateLastOnline, () -> {
          throw new NoSuchElementException("user status not found with id: " + userStatusId);
        });
  }
  
  @Override
  public void updateByUserId(UUID userId) {
    userStatusRepository.findByUserId(userId)
        .ifPresentOrElse(UserStatus::updateLastOnline, () -> {
          throw new NoSuchElementException("user status not found with user id: " + userId);
        });
  }
  
  @Override
  public void remove(UUID userStatusId) {
    userStatusRepository.remove(userStatusId);
  }
}
