package com.sprint.mission.discodeit.service.util;

import com.sprint.mission.discodeit.entity.status.user.UserStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.service.status.UserStateService;

public class UserStatusContextUtils {

  /**
   * 유저 상태 저장
   */
  public static void saveUserStatus(User savedMember) {
    UserStateService userStateService = SpringContextUtils.getBean(UserStateService.class);

    UserStatus userStatus = userStateService.create(new UserStatus(savedMember));
    savedMember.changeUserStatus(userStatus);
  }
}
