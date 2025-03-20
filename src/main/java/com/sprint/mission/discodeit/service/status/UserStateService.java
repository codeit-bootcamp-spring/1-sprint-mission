package com.sprint.mission.discodeit.service.status;

import com.sprint.mission.discodeit.dto.response.UserStatusDto;
import com.sprint.mission.discodeit.entity.status.user.UserStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.entitymapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStateService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  /**
   * - [x] 관련된 `User`가 존재하지 않으면 예외를 발생시킵니다. - [x] 같은 `User`와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
   */
  public UserStatus create(UserStatus userStatus) {
    User findUser = getFindUser(userStatus);

    UserStatus status = isExistsUserStatus(findUser);

    return userStatusRepository.save(status);
  }


  /**
   * 유저에 대한 객체가 이미 존재하는지
   */
  private UserStatus isExistsUserStatus(User findUser) {
    if (findUser.getStatus() == null) {
      return new UserStatus(findUser, Instant.now());
    }

    return userStatusRepository.findById(findUser.getStatus().getId())
        .orElseThrow(UserStatusNotFoundException::new);
  }

  /**
   * userstatus와 관련된 user 찾기
   */
  private User getFindUser(UserStatus userStatus) {
    return userRepository.findById(userStatus.getUser().getId())
        .orElseThrow(UserStatusNotFoundException::new);
  }

  /**
   * `id`로 조회합니다.
   */
  public UserStatusDto find(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id를 입력해주세요.");
    }

    UserStatus findUserstatus = userStatusRepository.findById(id)
        .orElseThrow(UserStatusNotFoundException::new);

    return UserStatusMapper.toDto(findUserstatus);
  }

  /**
   * 모든 객체를 조회합니다.
   */
  public List<UserStatusDto> findAll() {
    List<UserStatus> allUserState = userStatusRepository.findAll();

    return allUserState.stream()
        .map(UserStatusMapper::toDto)
        .toList();
  }

  /**
   * DTO를 활용해 파라미터를 그룹화합니다. - 수정 대상 객체의 `id` 파라미터, 수정할 값 파라미터
   */
  public UserStatusDto update(UserStatusDto userStatusDto) {
    User findUser = userRepository.findById(userStatusDto.userId())
        .orElseThrow(UserNotFoundException::new);

    findUser.getStatus().setLastActiveAt(userStatusDto.lastActiveAt());

    return UserStatusMapper.toDto(findUser.getStatus());
  }

  /**
   * userId로 특정 User 객체 업데이트
   */
  public void updateByUserId(UUID userId) {
    User findUser = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    findUser.getStatus().setLastActiveAt(Instant.now());
  }

  /**
   * `id`로 삭제합니다.
   */
  public void delete(UUID id) {
    userStatusRepository.deleteById(id);
  }
}
