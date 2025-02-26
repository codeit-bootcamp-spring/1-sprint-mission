package com.sprint.mission.discodeit.service.status;

import com.sprint.mission.discodeit.entity.status.user.UserStatUpdateRequest;
import com.sprint.mission.discodeit.entity.status.user.UserStatus;
import com.sprint.mission.discodeit.entity.status.user.UserStatusReponse;
import com.sprint.mission.discodeit.entity.status.user.UserStatusRequest;
import com.sprint.mission.discodeit.entity.status.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.status.user.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserCommonRequest;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStateService implements StatusService<UserStatus> {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  /**
   * - [x] 관련된 `User`가 존재하지 않으면 예외를 발생시킵니다. - [x] 같은 `User`와 관련된 객체가 이미 존재하면 예외를 발생시킵니다.
   */
  @Override
  public UserStatus create(Object request) {
    if (!(request instanceof UserStatusRequest userStatusRequest)) {
      throw new IllegalArgumentException("UserStatusRequest 객체가 아닙니다.");
    }

    hasUserState(userStatusRequest);

    UserStatus userStatus = UserStatus.createUserStatus(userStatusRequest.userId(),
        userStatusRequest.userName());
    return userStatusRepository.save(userStatus);
  }

  /**
   * `id`로 조회합니다.
   */
  @Override
  public UserStatus find(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id를 입력해주세요.");
    }

    UserStatus findUserState = userStatusRepository.findById(id);
    if (findUserState == null) {
      throw new IllegalArgumentException("찾는 UserStatus가 없습니다.");
    }

    return findUserState;
  }

  /**
   * 모든 객체를 조회합니다.
   */
  @Override
  public Map<UUID, UserStatus> findAll() {
    Map<UUID, UserStatus> allUserState = userStatusRepository.findAll();

    if (allUserState == null) {
      throw new IllegalArgumentException("모든 UserStatus를 찾을 수 없습니다.");
    }

    return allUserState;
  }

  /**
   * DTO를 활용해 파라미터를 그룹화합니다. - 수정 대상 객체의 `id` 파라미터, 수정할 값 파라미터
   */
  @Override
  public UserStatus update(Object request) {
    if (!(request instanceof UserStatusRequest userStatusRequest)) {
      throw new IllegalArgumentException("UserStatusRequest 객체가 아닙니다.");
    }
    hasUser(userStatusRequest);

    UserStatus userStatus = new UserStatus(userStatusRequest.userId());
    return userStatusRepository.updateState(userStatusRequest.userId(), userStatus);
  }

  /**
   * userName으로 상태 업데이트
   */
  public UserStatus updateByUserName(String userName, UserCommonRequest request) {
    if (userName == null) {
      throw new IllegalArgumentException("유저를 입력해주세요.");
    }

    UserStatus userStatus = userStatusRepository.findByUserName(userName);
    UserStatus updateUserStatus = userStatus.changeUserStatus(request.userName());

    return userStatusRepository.updateState(userName, updateUserStatus);
  }

  /**
   * userName과 state를 받아서 업데이트
   */
  public UserStatusReponse updateByUserName(String userName, UserStatusUpdateRequest state) {
    if (userName == null) {
      throw new IllegalArgumentException("유저를 입력해주세요.");
    }

    UserStatus userStatus = userStatusRepository.findByUserName(userName);
    userStatus.updateUserStatus(state.state());

    return getUserStatusReponse(userName, userStatus);
  }

  /**
   * userId로 상태 업데이트
   */
  public UserStatusUpdateResponse updateByUserId(UUID userId, UserStatUpdateRequest request) {
    if (userId == null) {
      throw new IllegalArgumentException("id를 입력해주세요.");
    }

    UserStatus userStatus = userStatusRepository.findById(userId);
    if (userStatus == null) {
      throw new UserStatusNotFoundException(userId);
    }

//    if (userStatus == null) {
//      userStatus = new UserStatus(userId);
//      userStatusRepository.save(userStatus);
//    }

    userStatus.updateUserStatus();
    UserStatus updateState = userStatusRepository.updateState(userId, userStatus);
    return new UserStatusUpdateResponse(updateState.getLastAccessTime());
  }

  /**
   * `id`로 삭제합니다.
   */
  @Override
  public void delete(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id를 입력해주세요.");
    }

    userStatusRepository.remove(id);
  }

  private void hasUserState(UserStatusRequest userStatusRequest) {
    if (userStatusRepository.findById(userStatusRequest.userId()) != null) {
      throw new IllegalArgumentException("이미 존재하는 유저입니다.");
    }
  }

  private void hasUser(UserStatusRequest request) {
    User findUser = userRepository.findUserById(request.userId());
    if (findUser == null) {
      throw new UserNotFoundException("유저가 존재하지 않습니다.");
    }
  }

  private UserStatusReponse getUserStatusReponse(String userName, UserStatus userStatus) {
    UserStatus updateState = userStatusRepository.updateState(userName, userStatus);
    return new UserStatusReponse(updateState.getUserName(), updateState.getState(),
        updateState.getLastAccessTime());
  }
}
