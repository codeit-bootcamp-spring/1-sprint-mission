package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDeleteFacadeImpl implements UserDeleteFacade {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;

  @Override
  public void delete(String userId) {
    User user = userService.findUserById(userId);

    log.info("[User Delete] : 요청 시작 id={}", userId);

    userService.deleteUser(userId);
    log.info("[User Delete] : 사용자 삭제 성공");

    userStatusService.deleteByUserId(userId);
    log.info("[User Delete] : 사용자 상태 삭제 성공");

    if(user.getProfileId() != null){
      binaryContentService.delete(user.getProfileId());
    }
  }
}
